package ru.dombuketa.filmslocaror;

import static ru.dombuketa.filmslocaror.view.notify.NotifyConsts.CHANNEL_ID;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import ru.dombuketa.filmslocaror.di.DaggerIAppComponent_J;
import ru.dombuketa.db_module.DaggerIDatabaseComponent_J;
import ru.dombuketa.db_module.DatabaseModule_J;
import ru.dombuketa.db_module.IDatabaseComponent_J;
import ru.dombuketa.db_module.api.IAppProvider_J;
import ru.dombuketa.filmslocaror.di.IAppComponent_J;
import ru.dombuketa.filmslocaror.di.modules.DomainModule_J;
import ru.dombuketa.net_module.DaggerIRemoteComponent_J;
import ru.dombuketa.net_module.IRemoteComponent_J;
import ru.dombuketa.net_module.RemoteModule_J;

public class App_J extends Application implements IAppProvider_J {
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
        IRemoteComponent_J remoteProvider_J = DaggerIRemoteComponent_J.create();
        IDatabaseComponent_J databaseProvider_j = DaggerIDatabaseComponent_J.builder().iAppProvider_J((IAppProvider_J) provideContext()).build();
        daggerj = DaggerIAppComponent_J.builder()
                //.remoteModule_J(new RemoteModule_J())
                .iRemoteProvider_J(remoteProvider_J)
                //.databaseModule_J(new DatabaseModule_J())
                .iDatabaseProvider_J(databaseProvider_j)
                .domainModule_J(new DomainModule_J(this))
                .build();
        initWatchLaterChannel();
    }

    void initWatchLaterChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "WatchLaterChannel";
            String desc = "FilmsLocator notification channel";
            Integer important = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, important);
            mChannel.setDescription(desc);
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);
        }
    }

    @Override
    public Context provideContext() {
        return this;
    }
}
