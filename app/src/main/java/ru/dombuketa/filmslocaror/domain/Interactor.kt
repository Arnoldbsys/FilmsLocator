package ru.dombuketa.filmslocaror.domain

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dombuketa.filmslocaror.data.API
import ru.dombuketa.filmslocaror.data.ITmdbApi
import ru.dombuketa.filmslocaror.data.MainRepository
import ru.dombuketa.filmslocaror.data.PreferenceProvider
import ru.dombuketa.filmslocaror.data.entity.TmdbResultsDTO
import ru.dombuketa.filmslocaror.viewmodel.HomeFragmentViewModel
import java.util.*

class Interactor(private val repo: MainRepository, private val retrofitService: ITmdbApi, private val preferences: PreferenceProvider) {
    //В конструктор мы будем передавать коллбэк из вью модели, чтобы реагировать на то, когда фильмы будут получены
    //и страницу, которую нужно загрузить (это для пагинации)
    val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    var progressBarState = Channel<Boolean> (Channel.CONFLATED)

    fun getFilmsFromAPI(page: Int){
        // Показываем ProgressBar
        scope.launch {
            progressBarState.send(true)
        }
        //Метод getDefaultCategoryFromPreferences() будет нам получать при каждом запросе нужный нам список фильмов
        retrofitService.getFilms(getDefaultCategoryFromPreferences(), API.KEY, "ru-RU", page).enqueue(object : Callback<TmdbResultsDTO>{
            override fun onResponse(call: Call<TmdbResultsDTO>, response: Response<TmdbResultsDTO>) {
                //42* отказ от конвертера val list = ConverterFilm.convertApiListToDTOList(response.body()?.tmdbFilms).asFlow();
                val list = response.body()?.tmdbFilms?.asFlow();
                val res  = list?.map { film -> Film(id = film.id, title = film.title,
                    poster = film.posterPath,
                    description = film.overview,
                    rating = film.voteAverage,
                    isInFavorites = false)
                }
                //42*_
                //При успехе мы вызываем метод передаем onSuccess и в этот коллбэк список фильмов
                scope.launch {
                    repo.putToDB(res)
                    progressBarState.send(false)
                    preferences.setLastTimeInternetOK(Date().time) //40*
                }
            }

            override fun onFailure(call: Call<TmdbResultsDTO>, t: Throwable) {
                //40*
                if ((Date().time - preferences.getLastTimeInternetOK()) / TIMEDEVIDER > TIME_ACTUAL_CACHE_MINUTES) {
                    println("!!! - Удаляем кэш - более " + TIME_ACTUAL_CACHE_MINUTES + " минут прошло.")
                    repo.clearAllinDB()
                }
                //40*_
                //В случае провала выключаем ProgressBar
                scope.launch {
                    progressBarState.send(false)
                }
            }

        })
    }

    fun getFilmsFromAPI(page: Int, callback: HomeFragmentViewModel.IApiCallback){
        //Метод getDefaultCategoryFromPreferences() будет нам получать при каждом запросе нужный нам список фильмов
        retrofitService.getFilms(getDefaultCategoryFromPreferences(), API.KEY, "ru-RU", page).enqueue(object : Callback<TmdbResultsDTO>{
            override fun onResponse(call: Call<TmdbResultsDTO>, response: Response<TmdbResultsDTO>) {
                //При успехе мы вызываем метод передаем onSuccess и в этот коллбэк список фильмов
                //42* отказ от конвертера val list = ConverterFilm.convertApiListToDTOList(response.body()?.tmdbFilms).asFlow();
                val list = response.body()?.tmdbFilms?.asFlow();
                val res  = list?.map { film -> Film(id = film.id, title = film.title,
                    poster = film.posterPath,
                    description = film.overview,
                    rating = film.voteAverage,
                    isInFavorites = false)
                }
                //42*_
                repo.putToDB(res)
                preferences.setLastTimeInternetOK(Date().time) //40*
                callback.onSuccess()
            }

            override fun onFailure(call: Call<TmdbResultsDTO>, t: Throwable) {
                //40*
                if ((Date().time - preferences.getLastTimeInternetOK()) / TIMEDEVIDER > TIME_ACTUAL_CACHE_MINUTES) {
                    println("!!! - Удаляем кэш - более " + TIME_ACTUAL_CACHE_MINUTES + " минут прошло.")
                    repo.clearAllinDB()
                }
                //40*_
                callback.onFailure()
            }

        })


    }


//Метод для сохранения настроек
    fun savaDefaultCategoryToPreferences(category: String) = preferences.saveDefaultCategory(category)
    //Метод для получения настроек
    fun getDefaultCategoryFromPreferences() = preferences.getDefaultCategory()
    //И вот такой метод у нас будет дергать метод репозитория, чтобы тот забрал для нас фильмы из БД
    fun getFilmsFromDB(): Flow<List<Film>> = repo.getAllFromDB()

    companion object{
        //40*
        private const val TIMEDEVIDER = 60000
        private const val TIME_ACTUAL_CACHE_MINUTES = 1440 //Сутки
        //40*_
    }
}