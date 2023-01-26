package ru.dombuketa.filmslocaror

import android.os.Bundle
import android.transition.Scene
import android.transition.Slide
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.dombuketa.filmslocaror.databinding.FragmentCastsBinding
import ru.dombuketa.filmslocaror.databinding.FragmentFavoritesBinding
import ru.dombuketa.filmslocaror.databinding.FragmentHomeBinding
import java.util.*


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
//    val filmsDataBase = mutableListOf<Film>()
    private lateinit var filmsAdapter: FilmListRecyclerAdapter

        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {
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
                            filmsAdapter.addItems((requireActivity() as MainActivity).dataBase)
                        }
                        //Фильтруем список на поискк подходящих сочетаний
                        val result = ((requireActivity() as MainActivity).dataBase).filter {
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
            filmsAdapter = FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener{
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
        filmsAdapter.addItems((requireActivity() as MainActivity).dataBase)
    }

}