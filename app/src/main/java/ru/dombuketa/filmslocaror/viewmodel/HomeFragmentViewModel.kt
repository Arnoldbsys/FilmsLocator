package ru.dombuketa.filmslocaror.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.AppBarLayout.Behavior
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
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
    lateinit var filmsListRxJavaData : Observable<List<Film>>

    @Inject lateinit var interactor: Interactor
    val showProgressBar: BehaviorSubject<Boolean>
    val errorNetworkConnection = SingleLiveEvent<String>() //41*

    //38*
    var currentCategory = MutableLiveData<String>()
    @Inject lateinit var prefs: PreferenceProvider
    //38*_

    init {
        App.instance.dagger.injectt(this)
        showProgressBar = interactor.progressBarStateRx
        filmsListRxJavaData = interactor.getFilmsFromDB()

        getFilms()
        //38*
        prefs!!.currentCategory.observeForever {
            currentCategory.value = prefs!!.currentCategory.value
        }
        //38*_
        errorNetworkConnection.postValue("") //41*
    }

    fun getFilms(){
        interactor.getFilmsFromAPI(1)
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

