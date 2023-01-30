package ru.dombuketa.filmslocaror.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.dombuketa.filmslocaror.FilmsDataBase
import ru.dombuketa.filmslocaror.view.rv_adapters.FilmListRecyclerAdapter
import ru.dombuketa.filmslocaror.view.MainActivity
import ru.dombuketa.filmslocaror.utils.TopSpacingItemDecoration
import ru.dombuketa.filmslocaror.databinding.FragmentFavoritesBinding
import ru.dombuketa.filmslocaror.domain.Film
import ru.dombuketa.filmslocaror.utils.AnimationHelper
import ru.dombuketa.filmslocaror.viewmodel.FavoritesFragmentViewModel

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(FavoritesFragmentViewModel::class.java)
    }
    private var filmsDataBase = listOf<Film>()
    set(value){
        if (field == value ) return
        field = value.filter { it.isInFavorites == true }
        filmsAdapter.addItems(field)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        viewModel.filmsListLiveData.observe(viewLifecycleOwner, Observer<List<Film>>{
            filmsDataBase = it
        })
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFavoriteRV()
        AnimationHelper.performFragmentCircularRevealAnimation(binding.favoriteFragmentRoot, requireActivity(), 2)
    }

    private fun initFavoriteRV() {
        binding.favoritesRecycler.apply {
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
            //Применяем декоратор для отступов
            val decorator = TopSpacingItemDecoration(8)
            addItemDecoration(decorator)
        }
        //Кладем нашу БД в RV
        filmsAdapter.addItems(filmsDataBase.filter { it.isInFavorites == true })
    }

}