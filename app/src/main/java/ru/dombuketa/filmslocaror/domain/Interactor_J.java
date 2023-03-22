package ru.dombuketa.filmslocaror.domain;

import android.os.Build;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import ru.dombuketa.filmslocaror.data.API;
import ru.dombuketa.filmslocaror.data.ITmdbApi_J;
import ru.dombuketa.filmslocaror.data.MainRepository_J;
import ru.dombuketa.filmslocaror.data.PreferenceProvider_J;
import ru.dombuketa.filmslocaror.data.entity.TmdbResultsDTO;
import ru.dombuketa.filmslocaror.utils.ConverterFilm_J;
import ru.dombuketa.filmslocaror.view.fragments.HomeFragment_J;
import ru.dombuketa.filmslocaror.viewmodel.HomeFragmentViewModel_J;

public class Interactor_J {
    private MainRepository_J repo;
    private ITmdbApi_J retrofitService;
    private PreferenceProvider_J preferences_j;

    //40*
    private final int TIMEDEVIDER = 60000;
    private final int TIME_ACTUAL_CACHE_MINUTES = 10;
    //40*_

    public Interactor_J(MainRepository_J repo, ITmdbApi_J retrofitService, PreferenceProvider_J prefs) {
        this.repo = repo;
        this.retrofitService = retrofitService;
        this.preferences_j = prefs;
    }

    public List<Film> getFilmsDB(){
        //return repo.filmsDataBase;
        return repo.getALLFromDB();
    }
    //40*
    public void clearFilmsDB(){
        repo.clearAllinDB();
    }
    //40*_
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
                callback.onSuc(list);
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
    List<Film> getFilmsFromDB(){
        return repo.getALLFromDB();
    }
}
