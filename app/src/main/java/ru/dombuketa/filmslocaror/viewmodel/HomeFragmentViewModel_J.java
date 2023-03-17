package ru.dombuketa.filmslocaror.viewmodel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.dombuketa.filmslocaror.App_J;
import ru.dombuketa.filmslocaror.data.PreferenceProvider_J;
import ru.dombuketa.filmslocaror.domain.Film;
import ru.dombuketa.filmslocaror.domain.Interactor_J;

public class HomeFragmentViewModel_J extends ViewModel {
    public MutableLiveData<List<Film>> filmsListLiveData = new MutableLiveData<List<Film>>();


    //private Interactor_J interactor = App_J.getInstance().interactor;
    @Inject public Interactor_J interactor;
    //38*
    public MutableLiveData<String> currentCategory = new MutableLiveData<>();
    @Inject public PreferenceProvider_J prefs;
    //38*_


    public HomeFragmentViewModel_J() {
        App_J.getInstance().daggerj.injectj(this);
        // для локальной БД
        //List<Film> films = interactor.getFilmsDB();
        //filmsListLiveData.postValue(films);
        // По сети в APP_J создать интерактор с другим конструктором
        getFilms();
        //38*
        prefs.currentCategory.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                currentCategory.setValue(prefs.currentCategory.getValue());
            }
        });
        //38*_

    }

    public void getFilms(){
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
