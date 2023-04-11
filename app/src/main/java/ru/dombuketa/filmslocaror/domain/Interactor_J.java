package ru.dombuketa.filmslocaror.domain;

import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import ru.dombuketa.filmslocaror.data.API;
import ru.dombuketa.filmslocaror.data.ITmdbApi_J;
import ru.dombuketa.filmslocaror.data.MainRepository_J;
import ru.dombuketa.filmslocaror.data.PreferenceProvider_J;
import ru.dombuketa.filmslocaror.data.entity.TmdbFilm;
import ru.dombuketa.filmslocaror.data.entity.TmdbResultsDTO;
import ru.dombuketa.filmslocaror.utils.ConverterFilm_J;
import ru.dombuketa.filmslocaror.viewmodel.HomeFragmentViewModel_J;

public class Interactor_J {
    private MainRepository_J repo;
    private ITmdbApi_J retrofitService;
    private PreferenceProvider_J preferences_j;

    //40*
    private final int TIMEDEVIDER = 60000;
    private final int TIME_ACTUAL_CACHE_MINUTES = 1440; //Сутки
    //40*_
    public BehaviorSubject<Integer> progressBarStateRx = BehaviorSubject.create();

    public Interactor_J(MainRepository_J repo, ITmdbApi_J retrofitService, PreferenceProvider_J prefs) {
        this.repo = repo;
        this.retrofitService = retrofitService;
        this.preferences_j = prefs;
    }

    public Observable<List<Film>> getFilmsDB(){
        //return repo.filmsDataBase;
        return repo.getALLFromDB();
    }
    //40*
    public void clearFilmsDB(){
        repo.clearAllinDB();
    }
    //40*_

    public void getFilmsFromApi(int page){
        progressBarStateRx.onNext(View.VISIBLE);

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        Log.i("getFilmsFromApi",stackTrace[2].getMethodName());

        retrofitService.getFilms(getDefaultCategoryFromPreferences(), API.KEY, "ru-RU", page).enqueue(new Callback<TmdbResultsDTO>() {
            @Override
            public void onResponse(Call<TmdbResultsDTO> call, Response<TmdbResultsDTO> response) {
                if (response.body() != null) {
                    List<TmdbFilm> listApi = response.body().getTmdbFilms();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        List<Film> listDTO = listApi.stream().map(
                            film -> new Film(film.getId(),
                                film.getTitle(),
                                film.getPosterPath(),
                                film.getOverview(),
                                film.getVoteAverage(),
                                false)).collect(Collectors.toList());
                        Completable.fromSingle(observer -> {
                            repo.putToDB(listDTO);
                        })
                        .subscribeOn(Schedulers.io()).subscribe();
                    }
                }
                progressBarStateRx.onNext(View.GONE);
                preferences_j.setLastTimeInternetOK(new Date().getTime()); //40*
                Log.i("interactor J","data from NET");
            }

            @Override
            public void onFailure(Call<TmdbResultsDTO> call, Throwable t) {
                //40*
                if ((new Date().getTime() - preferences_j.getLastTimeInternetOK()) / TIMEDEVIDER > TIME_ACTUAL_CACHE_MINUTES){
                    System.out.println("!!! - Удаляем кэш - более " + TIME_ACTUAL_CACHE_MINUTES + "минут прошло.");
                    repo.clearAllinDB();
                }
                //40*_
                progressBarStateRx.onNext(View.GONE);
                Log.i("interactor J","data from DB");
            }
        });
    }


    public void getFilmsFromApi(int page, HomeFragmentViewModel_J.IApiCallback callback){
        retrofitService.getFilms(getDefaultCategoryFromPreferences(), API.KEY, "ru-RU", page).enqueue(new Callback<TmdbResultsDTO>() {
            @Override
            public void onResponse(Call<TmdbResultsDTO> call, Response<TmdbResultsDTO> response) {
                List<Film> list = new ArrayList<>();
                if (response.body() != null) {
                    list = ConverterFilm_J.convertApiListToDTOList(response.body().getTmdbFilms());
//                    for (Film film : list) {
//                        repo.putToDB(film);
//                    }
                    repo.putToDB(list);
                }
                preferences_j.setLastTimeInternetOK(new Date().getTime()); //40*
                callback.onSuc();
            }

            @Override
            public void onFailure(Call<TmdbResultsDTO> call, Throwable t) {
                //40*
                if ((new Date().getTime() - preferences_j.getLastTimeInternetOK()) / TIMEDEVIDER > TIME_ACTUAL_CACHE_MINUTES){
                    System.out.println("!!! - Удаляем кэш - более " + TIME_ACTUAL_CACHE_MINUTES + "минут прошло.");
                    repo.clearAllinDB();
                }
                //40*_
                callback.onFal();
            }
        });
    }

    //Метод для сохранения настроек
    public void saveDefaultCategoryToPreferences(String category){
        preferences_j.saveDefaultCategory(category);
    }

    //Метод для получения настроек
    public String getDefaultCategoryFromPreferences(){
        return preferences_j.getDefaultCategory();
    }

    //И вот такой метод у нас будет дергать метод репозитория, чтобы тот забрал для нас фильмы из БД
    Observable<List<Film>> getFilmsFromDB(){
        return repo.getALLFromDB();
    }
}
