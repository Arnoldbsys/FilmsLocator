package ru.dombuketa.filmslocaror.domain;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import ru.dombuketa.filmslocaror.data.API;
import ru.dombuketa.filmslocaror.data.ITmdbApi_J;
import ru.dombuketa.filmslocaror.data.MainRepository_J;
import ru.dombuketa.filmslocaror.data.entity.TmdbResultsDTO;
import ru.dombuketa.filmslocaror.utils.ConverterFilm_J;
import ru.dombuketa.filmslocaror.view.fragments.HomeFragment_J;
import ru.dombuketa.filmslocaror.viewmodel.HomeFragmentViewModel_J;

public class Interactor_J {
    private MainRepository_J repo;
    private ITmdbApi_J retrofitService;
    //private HomeFragmentViewModel_J.IApiCallback
    public Interactor_J(MainRepository_J repo) {
        this.repo = repo;
    }
    public Interactor_J(MainRepository_J repo, ITmdbApi_J retrofitService) {
        this.repo = repo;
        this.retrofitService = retrofitService;
    }

    public List<Film> getFilmsDB(){
        return repo.filmsDataBase;
    }

    public void getFilmsFromApi(int page, HomeFragmentViewModel_J.IApiCallback callback){
        retrofitService.getFilms(API.KEY, "ru-RU", page).enqueue(new Callback<TmdbResultsDTO>() {
            @Override
            public void onResponse(Call<TmdbResultsDTO> call, Response<TmdbResultsDTO> response) {
                if (response.body() != null)
                    callback.onSuc(new ConverterFilm_J().convertApiListToDTOList(response.body().getTmdbFilms()));
            }

            @Override
            public void onFailure(Call<TmdbResultsDTO> call, Throwable t) {
                    callback.onFal();
            }
        });
    }
}
