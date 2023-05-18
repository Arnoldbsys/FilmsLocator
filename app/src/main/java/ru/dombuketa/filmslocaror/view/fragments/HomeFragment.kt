package ru.dombuketa.filmslocaror.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.dombuketa.filmslocaror.App
import ru.dombuketa.filmslocaror.databinding.FragmentHomeBinding
import ru.dombuketa.filmslocaror.utils.AnimationHelper
import ru.dombuketa.filmslocaror.utils.AutoDisposable
import ru.dombuketa.filmslocaror.utils.TopSpacingItemDecoration
import ru.dombuketa.filmslocaror.utils.addTo
import ru.dombuketa.filmslocaror.view.MainActivity
import ru.dombuketa.filmslocaror.view.rv_adapters.FilmListRecyclerAdapter
import ru.dombuketa.filmslocaror.viewmodel.HomeFragmentViewModel
import java.util.*
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var pageNumber = 1
    private var lastVisibleItem = 0
    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    private val autoDisposable = AutoDisposable()
    private lateinit var layoutManagerRV: LinearLayoutManager

    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(HomeFragmentViewModel::class.java)
    }
    private var filmsDataBase = listOf<Film>()
        //Используем backing field
        set(value) {
            //Если придет такое же значение, то мы выходим из метода
            if (field == value) return
            //Если пришло другое значение, то кладем его в переменную
            field = value
            //Обновляем RV адаптер
            filmsAdapter.addItems(field)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        autoDisposable.bindTo(lifecycle)
        //38*
        viewModel.currentCategory.observe(viewLifecycleOwner, {
            filmsAdapter.items.clear()
            viewModel.getFilms(pageNumber)
        })
        //38*_
        //41*
        viewModel.errorNetworkConnection.observe(viewLifecycleOwner, Observer<String> {
            if (!it.isNullOrEmpty()) {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearErrorConnectionError()
            }
        })
        //41*_
        App.instance.dagger.injectt(this)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchViewByAPI()
        AnimationHelper.performFragmentCircularRevealAnimation(binding.homeFragmentRoot, requireActivity(),1)
        initPullToRefresh()
        //Кладем нашу БД в RV
        viewModel.filmsListRxJavaData.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                filmsAdapter.addItems(list)
                filmsDataBase = list
            }
            .addTo(autoDisposable)
        viewModel.showProgressBar.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.progressBar.isVisible = it
            }
        initHomeRV()
    }

    private fun initSearchViewByAPI() {
        binding.searchView.setOnClickListener { binding.searchView.isIconified = false }
        io.reactivex.rxjava3.core.Observable.create(ObservableOnSubscribe<String> { subs ->
            binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    filmsAdapter.items.clear()
                    subs.onNext(newText)
                    return false
                }
                override fun onQueryTextSubmit(query: String?): Boolean {
                    filmsAdapter.items.clear()
                    subs.onNext(query)
                    return false
                }
            })
        }).subscribeOn(Schedulers.io())
            .map {
                it.toLowerCase(Locale.getDefault()).trim()
            }
            .debounce(1200, TimeUnit.MILLISECONDS)
            .filter {
                if (!it.isNotBlank()) viewModel.getFilms(1)
                it.isNotBlank()
            }
            .flatMap {
                viewModel.getFilmsBySearch(it, 1)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
// Коментарий оставил для ментора см. вопрос с сданном ДЗ
//            .onErrorReturn {
//                Log.e("InitSearchView", "onErrorReturn null" + it.message)
//                viewModel.showProgressBar.onNext(false)
//                return @onErrorReturn null
//            }
            .subscribeBy(
                onNext = {
                    filmsAdapter.addItems(it)
                    if (layoutManagerRV.itemCount > 0) {
                        layoutManagerRV.scrollToPosition(1)  //Если промотали, то вернуть к началу списка
                    }
                    pageNumber = 1  // для страничнинга :-)
                    Log.i("initSearchViewAPI", "search OK.")
                },
                onError = {
                    Toast.makeText(
                        requireContext(),
                        "Ошибка получения данных...{${it.message}}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.i("initSearchViewAPI", it.message.toString())
                })
            .addTo(autoDisposable)
    }

    private fun initPullToRefresh() {
        //Вешаем слушатель, чтобы вызвался pull to refresh
        binding.pullToRefresh.setOnRefreshListener {
            //Чистим адаптер(items нужно будет сделать паблик или создать для этого публичный метод)
            filmsAdapter.items.clear()
            val query = binding.searchView.query.toString().trim()
            //Делаем новый запрос фильмов на сервер
            if (query.isNotBlank()) {
                viewModel.getFilmsBySearch(query, 1)
                    .subscribeOn(Schedulers.io())
                    .subscribeBy(onNext = {
                        filmsAdapter.addItems(it)
                    }, onError = {
                        println(it.message)
                    })
            } else {
                viewModel.getFilms(1)
            }
            //Убираем крутящееся колечко
            binding.pullToRefresh.isRefreshing = false
        }
    }

    private fun initHomeRV() {
        //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
        //оставим его пока пустым, он нам понадобится во второй части задания
        binding.mainRecycler.apply {
            filmsAdapter =
                FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                    override fun click(film: Film) {
                        (requireActivity() as MainActivity).lanunchDetailsFragment(film)
                    }
                })
            //Присваиваем адаптер
            adapter = filmsAdapter
            //Присвои layoutmanager
            layoutManager = LinearLayoutManager(requireContext())
            layoutManagerRV = layoutManager as LinearLayoutManager
            //Применяем декоратор для отступов
            val decorator = TopSpacingItemDecoration(8)
            addItemDecoration(decorator)
        }
        initRvScroller()
    }

    private fun initRvScroller() {
        io.reactivex.rxjava3.core.Observable.create(ObservableOnSubscribe<Int> { page ->
            binding.mainRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                }
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0 && layoutManagerRV.findLastVisibleItemPosition() > lastVisibleItem) { // Прокрутка вниз.
                        lastVisibleItem = layoutManagerRV.findLastVisibleItemPosition()
                        Log.i("initRvScroller","!!!  totalItemCount=${layoutManagerRV.itemCount} lastVisiblesItems=$lastVisibleItem pageNumber=$pageNumber")
                        if (lastVisibleItem + FILMS_ITEM_SHIFT == FILMS_PER_PAGE * pageNumber - 1) {
                            page.onNext(++pageNumber); // viewModel.getFilms(++pageNumber) Сейчас передаем по потоку
                        }
                    }
                }
            })
        })
        .observeOn(Schedulers.io())
        .subscribeBy(
            onNext = {
                val query = binding.searchView.getQuery().toString().trim()
                if (query.isNotBlank()) {
                    viewModel.getFilmsBySearch(query, it)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                            onNext = {
                                filmsAdapter.addItems(it)
                            },
                            onError = {
                                Toast.makeText(requireContext(),"Ошибка загрузки данных из сети.", Toast.LENGTH_SHORT).show()
                                it.message?.let { it1 -> Log.e("subscribe 1", it1) }
                            })
                } else {
                    viewModel.getFilms(it)
                }
            },
            onError = {
                Toast.makeText(
                    requireContext(),
                    "Ошибка загрузки данных из сети.",
                    Toast.LENGTH_SHORT
                ).show()
                it.message?.let { it1 -> Log.e("subscribe 2", it1) }
            })
    }

    companion object {
        const val FILMS_ITEM_SHIFT: Int = 4
        const val FILMS_PER_PAGE: Int = 20
    }
}