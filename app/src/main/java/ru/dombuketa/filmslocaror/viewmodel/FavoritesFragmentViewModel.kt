package ru.dombuketa.filmslocaror.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dombuketa.db_module.dto.Film

class FavoritesFragmentViewModel : ViewModel() {
    val filmsListLiveData = MutableLiveData<List<Film>>()
    //private var interactor: Interactor = App.instance.interactor

    init {
        //TODO init interactor
        //val films = interactor.getFilmsDB() //.filter { it.isInFavorites == true }
        //filmsListLiveData.postValue(films)
    }
}