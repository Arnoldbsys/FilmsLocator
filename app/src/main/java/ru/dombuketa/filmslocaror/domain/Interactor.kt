package ru.dombuketa.filmslocaror.domain

import android.util.Log
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dombuketa.filmslocaror.data.API
import ru.dombuketa.filmslocaror.data.ITmdbApi
import ru.dombuketa.filmslocaror.data.MainRepository
import ru.dombuketa.filmslocaror.data.PreferenceProvider
import ru.dombuketa.filmslocaror.data.entity.TmdbResultsDTO
import java.util.*

class Interactor(private val repo: MainRepository, private val retrofitService: ITmdbApi, private val preferences: PreferenceProvider) {
    //В конструктор мы будем передавать коллбэк из вью модели, чтобы реагировать на то, когда фильмы будут получены
    //и страницу, которую нужно загрузить (это для пагинации)
    var progressBarStateRx : BehaviorSubject<Boolean> = BehaviorSubject.create()

    fun getFilmsFromAPI(page: Int){
        // Показываем ProgressBar
        progressBarStateRx.onNext(true)
        //Метод getDefaultCategoryFromPreferences() будет нам получать при каждом запросе нужный нам список фильмов
        retrofitService.getFilms(getDefaultCategoryFromPreferences(), API.KEY, "ru-RU", page).enqueue(object : Callback<TmdbResultsDTO>{
            override fun onResponse(call: Call<TmdbResultsDTO>, response: Response<TmdbResultsDTO>) {
                //44*
                val listApi = response.body()?.tmdbFilms
                val listDTO = listApi?.map { film ->
                    Film(id = film.id,
                        title = film.title,
                        poster = film.posterPath,
                        description = film.overview,
                        rating = film.voteAverage,
                        isInFavorites = false)
                }
                //44*_
                //При успехе мы вызываем метод передаем onSuccess и в этот коллбэк список фильмов
                Completable.fromSingle<List<Film>>{
                    repo.putToDB(listDTO)
                }.subscribeOn(Schedulers.io()).subscribe()
                progressBarStateRx.onNext(false)
                preferences.setLastTimeInternetOK(Date().time) //40*
                Log.i("interactor","data from NET")
            }

            override fun onFailure(call: Call<TmdbResultsDTO>, t: Throwable) {
                //40*
                if ((Date().time - preferences.getLastTimeInternetOK()) / TIMEDEVIDER > TIME_ACTUAL_CACHE_MINUTES) {
                    println("!!! - Удаляем кэш - более " + TIME_ACTUAL_CACHE_MINUTES + " минут прошло.")
                    repo.clearAllinDB()
                }
                //40*_
                //В случае провала выключаем ProgressBar
                progressBarStateRx.onNext(false)
                Log.i("interactor","data from DB")
            }

        })
    }

    //Метод для сохранения настроек
    fun savaDefaultCategoryToPreferences(category: String) = preferences.saveDefaultCategory(category)
    //Метод для получения настроек
    fun getDefaultCategoryFromPreferences() = preferences.getDefaultCategory()
    //И вот такой метод у нас будет дергать метод репозитория, чтобы тот забрал для нас фильмы из БД
    fun getFilmsFromDB(): Observable<List<Film>> = repo.getAllFromDB()

    companion object{
        //40*
        private const val TIMEDEVIDER = 60000
        private const val TIME_ACTUAL_CACHE_MINUTES = 1440 //Сутки
        //40*_
    }
}