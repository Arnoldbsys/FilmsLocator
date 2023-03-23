package ru.dombuketa.filmslocaror.viewmodel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import ru.dombuketa.filmslocaror.App_J;
import ru.dombuketa.filmslocaror.data.PreferenceProvider_J;
import ru.dombuketa.filmslocaror.domain.Film;
import ru.dombuketa.filmslocaror.domain.Interactor_J;

public class HomeFragmentViewModel_J extends ViewModel {
    //public MutableLiveData<List<Film>> filmsListLiveData = new MutableLiveData<List<Film>>();
    public LiveData<List<Film>> filmsListLiveData;
    public MutableLiveData<Integer> showProgressBar = new MutableLiveData<>();

    //private Interactor_J interactor = App_J.getInstance().interactor;
    @Inject public Interactor_J interactor;
    //38*
    public MutableLiveData<String> currentCategory = new MutableLiveData<>();
    @Inject public PreferenceProvider_J prefs;
    //38*_


    public HomeFragmentViewModel_J() {
        App_J.getInstance().daggerj.injectj(this);
        filmsListLiveData = interactor.getFilmsDB();
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
        showProgressBar.postValue(0);
        interactor.getFilmsFromApi(1, new IApiCallback() {
            @Override
            public void onSuc() {
                System.out.println("!!!J HomeFragVM - данные из сети");
                //filmsListLiveData.postValue(films);
                showProgressBar.postValue(8);
            }
            @Override
            public void onFal() {
                System.out.println("!!!J HomeFragVM - ошибка доступа к сети - данные из DB");
//                Executors.newSingleThreadExecutor().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        filmsListLiveData.postValue(interactor.getFilmsDB());
//                    }
//                });
                showProgressBar.postValue(8);
            }
        });
    }

    public interface IApiCallback{
        void onSuc();
        void onFal();
    }

}
