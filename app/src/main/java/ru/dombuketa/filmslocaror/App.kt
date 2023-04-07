package ru.dombuketa.filmslocaror

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.dombuketa.filmslocaror.data.ApiConstants
import ru.dombuketa.filmslocaror.data.ITmdbApi
import ru.dombuketa.filmslocaror.data.MainRepository
import ru.dombuketa.filmslocaror.di.DaggerIAppComponent
import ru.dombuketa.filmslocaror.di.IAppComponent
import ru.dombuketa.filmslocaror.di.modules.DatabaseModule
import ru.dombuketa.filmslocaror.di.modules.DomainModule
import ru.dombuketa.filmslocaror.di.modules.RemoteModule
import ru.dombuketa.filmslocaror.domain.Interactor
import java.util.concurrent.TimeUnit

class App : Application() {
    lateinit var dagger: IAppComponent
//    lateinit var repo: MainRepository
//    lateinit var interactor: Interactor
    override fun onCreate() {
        super.onCreate()
        //Инициализируем экземпляр App, через который будем получать доступ к остальным переменным
        instance = this
        //Создаём кастомный клиент
//        val  okHttpClient = OkHttpClient.Builder()
//            //Настраиваем таймауты для медленного интернета
//            .callTimeout(NETWORKTIMEOUT, TimeUnit.SECONDS)
//            .readTimeout(NETWORKTIMEOUT, TimeUnit.SECONDS)
//            //Добавляем логгер
//            .addInterceptor(HttpLoggingInterceptor().apply {
//                if (BuildConfig.DEBUG) {
//                    level = HttpLoggingInterceptor.Level.BASIC
//                }
//            })
//            .build()
        //Создаем Ретрофит
//        val retrofit = Retrofit.Builder()
//            //Указываем базовый URL из констант
//            .baseUrl(ApiConstants.BASE_URL)
//            //Добавляем конвертер
//            .addConverterFactory(GsonConverterFactory.create())
//            //Добавляем кастомный клиент
//            .client(okHttpClient)
//            .build()
        //Создаем сам сервис с методами для запросов
//        val retrofitService = retrofit.create(ITmdbApi::class.java)
        //Инициализируем репозиторий
//        repo = MainRepository()
        //Инициализируем интерактор
//        interactor = Interactor(repo, retrofitService)
        dagger = DaggerIAppComponent.builder()
            .remoteModule(RemoteModule())
            .databaseModule(DatabaseModule())
            .domainModule(DomainModule(this))
            .build()
    }

    companion object{
        //Здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
        //Приватный сеттер, чтобы нельзя было в эту переменную присвоить что-либо другое
        private set
        const val NETWORKTIMEOUT = 60L
    }
}