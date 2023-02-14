package ru.dombuketa.filmslocaror.domain

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dombuketa.filmslocaror.data.API
import ru.dombuketa.filmslocaror.data.ITmdbApi
import ru.dombuketa.filmslocaror.data.MainRepository
import ru.dombuketa.filmslocaror.data.entity.TmdbResultsDTO
import ru.dombuketa.filmslocaror.utils.ConverterFilm
import ru.dombuketa.filmslocaror.viewmodel.HomeFragmentViewModel

class Interactor(private val repo: MainRepository, private val retrofitService: ITmdbApi) {
    //fun getFilmsDB() : List<Film> = repo.filmsDataBase
    //В конструктор мы будем передавать коллбэк из вью модели, чтобы реагировать на то, когда фильмы будут получены
    //и страницу, которую нужно загрузить (это для пагинации)
    fun getFilmsFromAPI(page: Int, callback: HomeFragmentViewModel.ApiCallback){
        retrofitService.getFilms(API.KEY, "ru-RU", page).enqueue(object : Callback<TmdbResultsDTO>{
            override fun onResponse(call: Call<TmdbResultsDTO>, response: Response<TmdbResultsDTO>) {
                //При успехе мы вызываем метод передаем onSuccess и в этот коллбэк список фильмов
                callback.onSuccess(ConverterFilm.convertApiListToDTOList(response.body()?.tmdbFilms))
            }

            override fun onFailure(call: Call<TmdbResultsDTO>, t: Throwable) {
                callback.onFailure()
            }

        })


    }
}