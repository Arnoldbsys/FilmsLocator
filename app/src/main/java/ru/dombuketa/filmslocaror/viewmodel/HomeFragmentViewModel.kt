package ru.dombuketa.filmslocaror.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.dombuketa.filmslocaror.App
import ru.dombuketa.filmslocaror.data.PreferenceProvider
import ru.dombuketa.filmslocaror.domain.Film
import ru.dombuketa.filmslocaror.domain.Interactor
import ru.dombuketa.filmslocaror.utils.SingleLiveEvent
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.coroutines.EmptyCoroutineContext

class HomeFragmentViewModel : ViewModel() {
    lateinit var filmsListLiveData : LiveData<List<Film>>
    lateinit var filmsListFlowData : Flow<List<Film>>

    @Inject lateinit var interactor: Interactor
    //val showProgressBar: MutableLiveData<Boolean> = MutableLiveData()
    val showProgressBar: Channel<Boolean>
    val errorNetworkConnection = SingleLiveEvent<String>() //41*

    //38*
    var currentCategory = MutableLiveData<String>()
    @Inject lateinit var prefs: PreferenceProvider
    //38*_


    suspend fun <T> Flow<List<T>>.flattenToList() =
        flatMapConcat { it.asFlow() }.toList()

    init {
        App.instance.dagger.injectt(this)
        showProgressBar = interactor.progressBarState
        filmsListFlowData = interactor.getFilmsFromDB()

        getFilms()
        //38*
        prefs!!.currentCategory.observeForever {
            currentCategory.value = prefs!!.currentCategory.value
        }
        //38*_
        errorNetworkConnection.postValue("") //41*
    }

    fun getFilms(){
        //showProgressBar.postValue(true)
        interactor.getFilmsFromAPI(1)
//        interactor.getFilmsFromAPI(1, object : IApiCallback{
//            override fun onSuccess() {
//                println("!!! HomeFragVM - данные из сети")
//                //filmsListLiveData.postValue(films)
//                showProgressBar.postValue(false)
//            }
//
//            override fun onFailure() {
//                println("!!! HomeFragVM - ошибка доступа к сети - данные из DB")
////                Executors.newSingleThreadExecutor().execute {
////                    filmsListLiveData.postValue(interactor.getFilmsFromDB())
////                }
//                showProgressBar.postValue(false)
//                errorNetworkConnection.postValue(ERROR_CONNECTION_MSG) //41*
//            }
//
//        })
    }
    //41*
    fun clearErrorConnectionError() {
        errorNetworkConnection.postValue("")
    }
    //41*_

    interface IApiCallback{
        fun onSuccess()
        fun onFailure()
    }

    companion object{
        const val ERROR_CONNECTION_MSG = "Ошибка соединения." //41*
    }
}

