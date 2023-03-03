package ru.dombuketa.filmslocaror.di.modules;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.dombuketa.filmslocaror.App_J;
import ru.dombuketa.filmslocaror.BuildConfig;
import ru.dombuketa.filmslocaror.data.ApiConstants;
import ru.dombuketa.filmslocaror.data.ITmdbApi_J;

@Module
public class RemoteModule_J {
    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(){
        // Работа с сетью
        //Добавляем логгер
        HttpLoggingInterceptor logI = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) logI.setLevel(HttpLoggingInterceptor.Level.BASIC);
        //Создаём кастомный клиент
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .callTimeout(App_J.NETWORKTIMEOUT, TimeUnit.SECONDS) //Настраиваем таймауты для медленного интернета
                .readTimeout(App_J.NETWORKTIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logI)
                .build();
        return okHttpClient;
    }
    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL) //Указываем базовый URL из констант
                .addConverterFactory(GsonConverterFactory.create()) //Добавляем конвертер
                .client(okHttpClient) //Добавляем кастомный клиент
                .build();
        return retrofit;
    }
    @Singleton
    @Provides
    ITmdbApi_J provideTmdbApi(Retrofit retrofit){
        return retrofit.create(ITmdbApi_J.class);
    }


}
