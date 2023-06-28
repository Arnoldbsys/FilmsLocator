package ru.dombuketa.filmslocaror.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import ru.dombuketa.db_module.dto.Film
import ru.dombuketa.db_module.dto.Notification
import ru.dombuketa.filmslocaror.App
import ru.dombuketa.filmslocaror.domain.Interactor
import java.util.*
import javax.inject.Inject

class SeelaterFragmentViewModel : ViewModel() {
    lateinit var notificationsListRxJavaData : Observable<List<Notification>>
    @Inject lateinit var interactor: Interactor

    init {
        App.instance.dagger.injectt(this)
        notificationsListRxJavaData = interactor.getNotifications()
    }

    fun clearAllNotifications() = interactor.deactivateAllNotifications()

    fun getFilm(id: Int) : Observable<Film> {
        return interactor.getFilmFromAPI(id)
    }

    fun getEvaluatePeriodState() : Boolean = Date().time - interactor.getStartAppTimeFromPreferences() < TIME_EVALUATE_PERIOD

    fun getFilms(page: Int){
        //interactor.getFilmsFromAPI(page)
        interactor.getFilmsFromAPIRx(page)
    }

    companion object{
        private const val TIME_EVALUATE_PERIOD = 60000L //Минута
    }
}

