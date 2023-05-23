package ru.dombuketa.filmslocaror.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import ru.dombuketa.db_module.dto.Film;
import ru.dombuketa.filmslocaror.App_J;
import ru.dombuketa.filmslocaror.data.PreferenceProvider_J;
import ru.dombuketa.filmslocaror.domain.Interactor_J;
import ru.dombuketa.filmslocaror.utils.SingleLiveEvent_J;

public class HomeFragmentViewModel_J extends ViewModel {
    //public MutableLiveData<List<Film>> filmsListLiveData = new MutableLiveData<List<Film>>();
    private final String ERROR_CONNECTION_MSG = "Ошибка соединения."; //41*
    //public LiveData<List<Film>> filmsListLiveData;
    public Observable<List<Film>> filmsListLiveDataRx;
    public BehaviorSubject<Integer> showProgressBar;
    public SingleLiveEvent_J<String> errorNetworkConnection = new SingleLiveEvent_J<>(); //41*

    //private Interactor_J interactor = App_J.getInstance().interactor;
    @Inject public Interactor_J interactor;
    //38*
    public MutableLiveData<String> currentCategory = new MutableLiveData<>();
    @Inject public PreferenceProvider_J prefs;
    //38*_


    public HomeFragmentViewModel_J() {
        App_J.getInstance().daggerj.injectj(this);
        showProgressBar = interactor.progressBarStateRx;
        filmsListLiveDataRx = interactor.getFilmsDB();
        // для локальной БД
        //List<Film> films = interactor.getFilmsDB();
        //filmsListLiveData.postValue(films);
        // По сети в APP_J создать интерактор с другим конструктором
        //getFilms();
        //38*
        prefs.currentCategory.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                currentCategory.setValue(prefs.currentCategory.getValue());
            }
        });
        //38*_
        errorNetworkConnection.postValue(""); //41*
    }

    public void getFilms(Integer page){
        //interactor.getFilmsFromApi(page);
        interactor.getFilmsFromApiRx(page);
    }

    public void getFilmsRx(Integer page){
        interactor.getFilmsFromApiRx(page);
    }

    public Observable<List<Film>> getFilmsBySearch(String searchString, Integer page){
        return interactor.getFilmsFromApiBySearch(searchString, page);
    }

    //41*
    public void clearErrorConnectionError(){
        errorNetworkConnection.postValue("");
    }
    //41*_

    public interface IApiCallback{
        void onSuc();
        void onFal();
    }

}
