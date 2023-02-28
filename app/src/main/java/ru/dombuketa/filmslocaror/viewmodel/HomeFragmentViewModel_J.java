package ru.dombuketa.filmslocaror.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ru.dombuketa.filmslocaror.App_J;
import ru.dombuketa.filmslocaror.domain.Film;
import ru.dombuketa.filmslocaror.domain.Interactor_J;

public class HomeFragmentViewModel_J extends ViewModel {
    public MutableLiveData<List<Film>> filmsListLiveData = new MutableLiveData<List<Film>>();
    private Interactor_J interactor = App_J.getInstance().interactor;

    public HomeFragmentViewModel_J() {
        // для локальной БД
        //List<Film> films = interactor.getFilmsDB();
        //filmsListLiveData.postValue(films);
        // По сети в APP_J создать интерактор с другим конструктором
        interactor.getFilmsFromApi(1, new IApiCallback() {
            @Override
            public void onSuc(List<Film> films) {
                filmsListLiveData.postValue(films);
            }
            @Override
            public void onFal() {
                System.out.println("!!! ошибка сервиса");
            }
        });

    }


    public interface IApiCallback{
        void onSuc(List<Film> films);
        void onFal();
    }

}
