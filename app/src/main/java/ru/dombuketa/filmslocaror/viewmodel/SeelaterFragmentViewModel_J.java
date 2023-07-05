package ru.dombuketa.filmslocaror.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import ru.dombuketa.db_module.dto.Film;
import ru.dombuketa.db_module.dto.Notification;
import ru.dombuketa.filmslocaror.App_J;
import ru.dombuketa.filmslocaror.domain.Interactor_J;

public class SeelaterFragmentViewModel_J extends ViewModel {
    public Observable<List<Notification>> notificationsListRxJavaData;
    @Inject Interactor_J interactor_j;

    public SeelaterFragmentViewModel_J() {
        App_J.getInstance().daggerj.injectt(this);
        notificationsListRxJavaData = interactor_j.getNotifications();
    }

    public void clearAllNotifications() { interactor_j.deactivateAllNotifications(); }

    public Observable<Film> getFilm(int id)  {
        return interactor_j.getFilmFromAPI(id);
    }

}
