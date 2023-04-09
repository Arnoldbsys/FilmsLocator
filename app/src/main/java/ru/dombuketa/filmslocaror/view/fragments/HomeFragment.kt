package ru.dombuketa.filmslocaror.view.fragments

import android.os.Bundle
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
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.dombuketa.filmslocaror.App
import ru.dombuketa.filmslocaror.databinding.FragmentHomeBinding
import ru.dombuketa.filmslocaror.domain.Film
import ru.dombuketa.filmslocaror.domain.Interactor
import ru.dombuketa.filmslocaror.utils.AnimationHelper
import ru.dombuketa.filmslocaror.utils.AutoDisposable
import ru.dombuketa.filmslocaror.utils.TopSpacingItemDecoration
import ru.dombuketa.filmslocaror.utils.addTo
import ru.dombuketa.filmslocaror.view.MainActivity
import ru.dombuketa.filmslocaror.view.rv_adapters.FilmListRecyclerAdapter
import ru.dombuketa.filmslocaror.viewmodel.HomeFragmentViewModel
import java.util.*
import javax.inject.Inject

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var pageNumber = 1
    private var lastVisibleItem = 0
    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    @Inject lateinit var interactor: Interactor
    private val autoDisposable = AutoDisposable()

    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(HomeFragmentViewModel::class.java)
    }
    private var filmsDataBase = listOf<Film>()
    //Используем backing field
    set(value){
        //Если придет такое же значение, то мы выходим из метода
        if (field == value) return
        //Если пришло другое значение, то кладем его в переменную
        field = value
        //Обновляем RV адаптер
        filmsAdapter.addItems(field)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        autoDisposable.bindTo(lifecycle)
        //38*
        viewModel.currentCategory.observe(viewLifecycleOwner, {
            filmsAdapter.items.clear()
            viewModel.getFilms()
        })
        //38*_
        //41*
        viewModel.errorNetworkConnection.observe(viewLifecycleOwner, Observer<String>{
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
        initSearchView()
        AnimationHelper.performFragmentCircularRevealAnimation(binding.homeFragmentRoot, requireActivity(),1)
        initPullToRefresh()
        //Кладем нашу БД в RV
        viewModel.filmsListRxJavaData.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe{ list ->
                filmsAdapter.addItems(list)
                filmsDataBase = list
            }
            .addTo(autoDisposable)
        viewModel.showProgressBar.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                binding.progressBar.isVisible = it
            }
        initHomeRV()
    }

    private fun initSearchView() {
        binding.searchView.apply {
            setOnClickListener { this.isIconified = false }
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        //Если ввод пуст то вставляем в адаптер всю БД
                        if (newText.isEmpty()){
                            filmsAdapter.addItems(filmsDataBase)
                        }
                        //Фильтруем список на поискк подходящих сочетаний
                        val result = filmsDataBase.filter {
                            //Чтобы все работало правильно, нужно и запрос, и имя фильма приводить к нижнему регистру
                            it.title.toLowerCase(Locale.getDefault()).contains((newText.toLowerCase(Locale.getDefault())))
                        }
                        filmsAdapter.addItems(result)
                    }
                    return true
                }
            })
        }
    }

    private fun initPullToRefresh(){
        //Вешаем слушатель, чтобы вызвался pull to refresh
        binding.pullToRefresh.setOnRefreshListener {
            //Чистим адаптер(items нужно будет сделать паблик или создать для этого публичный метод)
            filmsAdapter.items.clear()
            //Делаем новый запрос фильмов на сервер
            viewModel.getFilms()
            //Убираем крутящееся колечко
            binding.pullToRefresh.isRefreshing = false
        }
    }

    private fun initHomeRV() {
        //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
        //оставим его пока пустым, он нам понадобится во второй части задания
        binding.mainRecycler.apply {
            filmsAdapter = FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                override fun click(film: Film) {
                    (requireActivity() as MainActivity).lanunchDetailsFragment(film)
                }
            })
            //Присваиваем адаптер
            adapter = filmsAdapter
            //Присвои layoutmanager
            layoutManager = LinearLayoutManager(requireContext())
            //Применяем декоратор для отступов
            val decorator = TopSpacingItemDecoration(8)
            addItemDecoration(decorator)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                }
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if ( dy > 0 && (layoutManager as LinearLayoutManager).findLastVisibleItemPosition() > lastVisibleItem) { // Прокрутка вниз.
                        lastVisibleItem = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                        if (lastVisibleItem + FILMS_ITEM_SHIFT == FILMS_PER_PAGE * pageNumber - 1){
/*
                            interactor.getFilmsFromAPI(pageNumber + 1, object : HomeFragmentViewModel.IApiCallback{
                                override fun onSuccess() {
//                                    val newfilmsDataBase: MutableList<Film> = viewModel.filmsListLiveData.value as MutableList<Film>
//                                    newfilmsDataBase.addAll(films)
//                                    viewModel.filmsListLiveData.postValue(newfilmsDataBase)
//                                    filmsAdapter.addItems(newfilmsDataBase)
//                                    pageNumber++
                                }
                                override fun onFailure() {
                                    println("!!! Error connection from HomeFrag")
                                }
                            })
*/
                        }
                    }
                }
            })
        }
    }

    companion object {
        const val FILMS_ITEM_SHIFT: Int = 4
        const val FILMS_PER_PAGE: Int = 20
    }
}