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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class HomeFragment : Fragment() {
//    val filmsDataBase = mutableListOf<Film>()
    private lateinit var filmsAdapter: FilmListRecyclerAdapter

        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val home_fragment_root = requireActivity().findViewById<ConstraintLayout>(R.id.home_fragment_root)
        val scene = Scene.getSceneForLayout(home_fragment_root, R.layout.merge_home_screen_content, requireContext())
        //Создаем анимацию выезда поля поиска сверху
        val searchSlider = Slide(Gravity.TOP).addTarget(R.id.search_view)
        //Создаем анимацию выезда RV снизу
        val recyclerSlider = Slide(Gravity.BOTTOM).addTarget(R.id.main_recycler)
        //Создаем экземпляр TransitionSet, который объединит все наши анимации
        val customTransition = TransitionSet().apply {
            duration = 500
            addTransition(recyclerSlider)
            addTransition(searchSlider)
        }
        //scene.enter()
        TransitionManager.go(scene, customTransition)

        initSearchView()
        initRV()

    }

    private fun initSearchView() {
        val search_view = requireView().findViewById<SearchView>(R.id.search_view)
        search_view.setOnClickListener{ search_view.isIconified = false}
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
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

    private fun initRV() {
        val main_recycler = (requireContext() as MainActivity).findViewById<RecyclerView>(R.id.main_recycler)
        //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
        //оставим его пока пустым, он нам понадобится во второй части задания
        main_recycler.apply {
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