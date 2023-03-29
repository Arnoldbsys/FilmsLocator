package ru.dombuketa.filmslocaror.domain

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dombuketa.filmslocaror.data.API
import ru.dombuketa.filmslocaror.data.ITmdbApi
import ru.dombuketa.filmslocaror.data.MainRepository
import ru.dombuketa.filmslocaror.data.PreferenceProvider
import ru.dombuketa.filmslocaror.data.entity.TmdbResultsDTO
import ru.dombuketa.filmslocaror.utils.ConverterFilm
import ru.dombuketa.filmslocaror.viewmodel.HomeFragmentViewModel
import java.util.*

class Interactor(private val repo: MainRepository, private val retrofitService: ITmdbApi, private val preferences: PreferenceProvider) {
    //В конструктор мы будем передавать коллбэк из вью модели, чтобы реагировать на то, когда фильмы будут получены
    //и страницу, которую нужно загрузить (это для пагинации)
    fun getFilmsFromAPI(page: Int, callback: HomeFragmentViewModel.IApiCallback){
        //Метод getDefaultCategoryFromPreferences() будет нам получать при каждом запросе нужный нам список фильмов
        retrofitService.getFilms(getDefaultCategoryFromPreferences(), API.KEY, "ru-RU", page).enqueue(object : Callback<TmdbResultsDTO>{
            override fun onResponse(call: Call<TmdbResultsDTO>, response: Response<TmdbResultsDTO>) {
                //При успехе мы вызываем метод передаем onSuccess и в этот коллбэк список фильмов
                val list = ConverterFilm.convertApiListToDTOList(response.body()?.tmdbFilms);
                repo.putToDB(list)
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
    fun getFilmsFromDB(): LiveData<List<Film>> = repo.getAllFromDB()

    companion object{
        //40*
        private const val TIMEDEVIDER = 60000
        private const val TIME_ACTUAL_CACHE_MINUTES = 1440 //Сутки
        //40*_
    }
}