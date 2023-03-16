package ru.dombuketa.filmslocaror;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.dombuketa.filmslocaror.data.ApiConstants;
import ru.dombuketa.filmslocaror.data.ITmdbApi_J;
import ru.dombuketa.filmslocaror.data.MainRepository_J;
import ru.dombuketa.filmslocaror.di.DaggerIAppComponent_J;
import ru.dombuketa.filmslocaror.di.IAppComponent_J;
import ru.dombuketa.filmslocaror.di.modules.DatabaseModule_J;
import ru.dombuketa.filmslocaror.di.modules.DomainModule_J;
import ru.dombuketa.filmslocaror.di.modules.RemoteModule_J;
import ru.dombuketa.filmslocaror.domain.Interactor_J;

public class App_J extends Application {
    public static final long NETWORKTIMEOUT = 60L;
    private static App_J instance;
    public static App_J getInstance() {
        return instance;
    }

//    public MainRepository_J repo;
//    public Interactor_J interactor;
    public IAppComponent_J daggerj;

    @Override
    public void onCreate() {
        super.onCreate();
        //Инициализируем экземпляр App, через который будем получать доступ к остальным переменным
        instance = this;
        // Работа с сетью
        //Добавляем логгер
//        HttpLoggingInterceptor logI = new HttpLoggingInterceptor();
//        if (BuildConfig.DEBUG) logI.setLevel(HttpLoggingInterceptor.Level.BASIC);
//        //Создаём кастомный клиент
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .callTimeout(NETWORKTIMEOUT, TimeUnit.SECONDS) //Настраиваем таймауты для медленного интернета
//                .readTimeout(NETWORKTIMEOUT, TimeUnit.SECONDS)
//                .addInterceptor(logI)
//                .build();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(ApiConstants.BASE_URL) //Указываем базовый URL из констант
//                .addConverterFactory(GsonConverterFactory.create()) //Добавляем конвертер
//                .client(okHttpClient) //Добавляем кастомный клиент
//                .build();
//        ITmdbApi_J retrofitService = retrofit.create(ITmdbApi_J.class);

        //Инициализируем репозиторий
//        repo = new MainRepository_J();
        //Инициализируем интерактор
        //interactor = new Interactor_J(repo); // локальная БД
//        interactor = new Interactor_J(repo, retrofitService);
        //daggerj = DaggerIAppComponent_J.create();
        daggerj = DaggerIAppComponent_J.builder()
                .remoteModule_J(new RemoteModule_J())
                .databaseModule_J(new DatabaseModule_J())
                .domainModule_J(new DomainModule_J(this))
                .build();
    }
}
