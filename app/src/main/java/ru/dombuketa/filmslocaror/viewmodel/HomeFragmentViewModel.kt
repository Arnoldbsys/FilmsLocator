package ru.dombuketa.filmslocaror.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dombuketa.filmslocaror.App
import ru.dombuketa.filmslocaror.data.PreferenceProvider
import ru.dombuketa.filmslocaror.domain.Film
import ru.dombuketa.filmslocaror.domain.Interactor
import javax.inject.Inject

class HomeFragmentViewModel : ViewModel() {
    val filmsListLiveData = MutableLiveData<List<Film>>()
    @Inject lateinit var interactor: Interactor

    //38*
    var currentCategory = MutableLiveData<String>()
    @Inject lateinit var prefs: PreferenceProvider
    //38*_

    init {
        App.instance.dagger.injectt(this)
        getFilms()
        //38*
        prefs!!.currentCategory.observeForever {
            currentCategory.value = prefs!!.currentCategory.value
        }
        //38*_
    }

    fun getFilms(){
        interactor.getFilmsFromAPI(1, object : ApiCallback{
            override fun onSuccess(films: List<Film>) {
                println("!!! HomeFragVM - данные из сети")
                filmsListLiveData.postValue(films)
            }

            override fun onFailure() {
                println("!!! HomeFragVM - ошибка доступа к сети - данные из DB")
                filmsListLiveData.postValue(interactor.getFilmsFromDB())
            }

        })
    }


    interface ApiCallback{
        fun onSuccess(films: List<Film>)
        fun onFailure()
    }
}

