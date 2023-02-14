package ru.dombuketa.filmslocaror.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dombuketa.filmslocaror.App
import ru.dombuketa.filmslocaror.domain.Film
import ru.dombuketa.filmslocaror.domain.Interactor

class HomeFragmentViewModel : ViewModel() {
    val filmsListLiveData = MutableLiveData<List<Film>>()
    private var interactor: Interactor = App.instance.interactor

    init {
        interactor.getFilmsFromAPI(1, object : ApiCallback{
            override fun onSuccess(films: List<Film>) {
                filmsListLiveData.postValue(films)
            }

            override fun onFailure() {
                println("!!! ошибка сервиса")
            }

        })
    }

    interface ApiCallback{
        fun onSuccess(films: List<Film>)
        fun onFailure()
    }
}

