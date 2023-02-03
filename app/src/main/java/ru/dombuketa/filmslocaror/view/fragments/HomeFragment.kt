package ru.dombuketa.filmslocaror.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.dombuketa.filmslocaror.view.rv_adapters.FilmListRecyclerAdapter
import ru.dombuketa.filmslocaror.view.MainActivity
import ru.dombuketa.filmslocaror.utils.TopSpacingItemDecoration
import ru.dombuketa.filmslocaror.databinding.FragmentHomeBinding
import ru.dombuketa.filmslocaror.domain.Film
import ru.dombuketa.filmslocaror.utils.AnimationHelper
import ru.dombuketa.filmslocaror.viewmodel.HomeFragmentViewModel
import java.util.*


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

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

    private lateinit var filmsAdapter: FilmListRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        viewModel.filmsListLiveData.observe(viewLifecycleOwner, Observer<List<Film>>{
            filmsDataBase = it
        })

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchView()
        initHomeRV()
        AnimationHelper.performFragmentCircularRevealAnimation(binding.homeFragmentRoot, requireActivity(),1)
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
            layoutManager= LinearLayoutManager(requireContext())
            //Применяем декоратор для отступов
            val decorator = TopSpacingItemDecoration(8)
            addItemDecoration(decorator)
        }
        //filmsAdapter.addItems((requireActivity() as MainActivity).dataBase)
    }

}