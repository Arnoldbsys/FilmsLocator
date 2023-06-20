package ru.dombuketa.filmslocaror.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import ru.dombuketa.db_module.dto.Film
import ru.dombuketa.db_module.dto.Notification
import ru.dombuketa.filmslocaror.App
import ru.dombuketa.filmslocaror.domain.Interactor
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

    fun getFilms(page: Int){
        //interactor.getFilmsFromAPI(page)
        interactor.getFilmsFromAPIRx(page)
    }

}

