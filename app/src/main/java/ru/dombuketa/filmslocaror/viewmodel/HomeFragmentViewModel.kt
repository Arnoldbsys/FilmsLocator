package ru.dombuketa.filmslocaror.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dombuketa.filmslocaror.App
import ru.dombuketa.filmslocaror.domain.Film
import ru.dombuketa.filmslocaror.domain.Interactor
import javax.inject.Inject

class HomeFragmentViewModel : ViewModel() {
    val filmsListLiveData = MutableLiveData<List<Film>>()
    //private var interactor: Interactor = App.instance.interactor
    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.injectt(this)
        interactor.getFilmsFromAPI(1, object : ApiCallback{
            override fun onSuccess(films: List<Film>) {
                filmsListLiveData.postValue(films)
            }

            override fun onFailure() {
                println("!!! ошибка сервиса from HomeFragVM")
            }

        })
    }

    interface ApiCallback{
        fun onSuccess(films: List<Film>)
        fun onFailure()
    }
}

