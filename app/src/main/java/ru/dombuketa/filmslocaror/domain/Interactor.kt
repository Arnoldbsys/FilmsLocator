package ru.dombuketa.filmslocaror.domain

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import ru.dombuketa.db_module.dto.Film
import ru.dombuketa.db_module.dto.Notification
import ru.dombuketa.db_module.repos.MainRepository
import ru.dombuketa.filmslocaror.data.API
import ru.dombuketa.net_module.ITmdbApi
import ru.dombuketa.filmslocaror.data.PreferenceProvider
import ru.dombuketa.filmslocaror.utils.ConverterFilm

class Interactor(private val repo: MainRepository, private val retrofitService: ITmdbApi, private val preferences: PreferenceProvider) {
    //В конструктор мы будем передавать коллбэк из вью модели, чтобы реагировать на то, когда фильмы будут получены
    //и страницу, которую нужно загрузить (это для пагинации)
    var progressBarStateRx : BehaviorSubject<Boolean> = BehaviorSubject.create()
    var evaluatePeriodStateRx : BehaviorSubject<Boolean> = BehaviorSubject.create()  // Пробный период

/*
    fun getFilmsFromAPI(page: Int){
        // Показываем ProgressBar
        progressBarStateRx.onNext(true)
        //Метод getDefaultCategoryFromPreferences() будет нам получать при каждом запросе нужный нам список фильмов
        retrofitService.getFilms(getDefaultCategoryFromPreferences(), API.KEY, "ru-RU", page).enqueue(object : Callback<ru.dombuketa.net_module.entity.TmdbResultsDTO>{
            override fun onResponse(call: Call<ru.dombuketa.net_module.entity.TmdbResultsDTO>, response: Response<ru.dombuketa.net_module.entity.TmdbResultsDTO>) {
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

            override fun onFailure(call: Call<ru.dombuketa.net_module.entity.TmdbResultsDTO>, t: Throwable) {
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
*/



    fun getFilmFromAPI(id: Int) : Observable<Film> {
        progressBarStateRx.onNext(true)
        return retrofitService.getFilm(id, API.KEY, "ru-RU")
            .subscribeOn(Schedulers.io())
            .map {
                progressBarStateRx.onNext(false)
                ConverterFilm.convertApiFilmToDTOFilm(it)
            }
            .doOnError { progressBarStateRx.onNext(false) }
        //return ConverterFilm.convertApiFilmToDTOFilm(retrofitService.getFilm(id, API.KEY, "ru-RU"))
    }

    fun getFilmsFromAPIRx(page: Int){
        progressBarStateRx.onNext(true)
        retrofitService.getFilmsRx(getDefaultCategoryFromPreferences(), API.KEY, "ru-RU", page)
            .subscribeOn(Schedulers.io())
            .map {
                ConverterFilm.convertApiListToDTOList(it.tmdbFilms)
            }
            .subscribeBy(onError = { progressBarStateRx.onNext(false)}, onNext = {
                progressBarStateRx.onNext(false)
                repo.putToDB(it)
            })
    }



    fun getFilmsFromAPIBySearch(searchString: String, page: Int) : Observable<List<Film>>{
        progressBarStateRx.onNext(true)
        return retrofitService.getFilmsBySearch(API.KEY, "ru-RU", searchString, 1)
            .map {
                progressBarStateRx.onNext(false)
                ConverterFilm.convertApiListToDTOList(it.tmdbFilms)
            }
    }

    //Метод для сохранения настроек
    fun savaDefaultCategoryToPreferences(category: String) = preferences.saveDefaultCategory(category)
    //Метод для получения настроек
    fun getDefaultCategoryFromPreferences() = preferences.getDefaultCategory()

    fun getStartAppTimeFromPreferences() = preferences.getStartTimeApp()
    fun setStartAppTimeToPreferences(time: Long) = preferences.setStartTimeApp(time)

    //И вот такой метод у нас будет дергать метод репозитория, чтобы тот забрал для нас фильмы из БД
    fun getFilmsFromDB(): Observable<List<Film>> = repo.getAllFromDB()

    fun getNotifications(): Observable<kotlin.collections.List<Notification>>{
        return repo.getActiveNotifications()
    }
    fun putNodificationtoDB(n: Notification) {
        Single.just(true)
            .observeOn(Schedulers.io())
            .subscribe( {
                repo.putNotificationToDB(n)
                println("!!! Нотификация сохранена в БД")
            },{
                println("!!! ОШИБКА: Нотификация не сохранена в БД" + it.message)
            })
    }
    fun deactivateNotification(film_id: Int){
        Single.just(true)
            .observeOn(Schedulers.io())
            .subscribe( {
                repo.deactivateNotification(film_id)
                println("!!! Нотификация деактивирована в БД")
            },{
                println("!!! ОШИБКА: Нотификация не деактивирована в БД" + it.message)
            })
    }
    fun deactivateAllNotifications(){
        Single.just(true)
            .observeOn(Schedulers.io())
            .subscribe( {
                repo.deactivateAllNotification()
                println("!!! Все нотификации деактивированы в БД")
            },{
                println("!!! ОШИБКА: Нотификации не деактивированы в БД" + it.message)
            })
    }
    companion object{
        //40*
        private const val TIMEDEVIDER = 60000
        private const val TIME_ACTUAL_CACHE_MINUTES = 1440 //Сутки
        //40*_
    }
}