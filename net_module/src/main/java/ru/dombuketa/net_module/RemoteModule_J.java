package ru.dombuketa.net_module;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.dombuketa.net_module.entity.ApiConstants;

@Module
public class RemoteModule_J {
    public static final long NETWORKTIMEOUT = 60L;

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(){
        // Работа с сетью
        //Добавляем логгер
        HttpLoggingInterceptor logI = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) logI.setLevel(HttpLoggingInterceptor.Level.BASIC);
        //Создаём кастомный клиент
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .callTimeout(NETWORKTIMEOUT, TimeUnit.SECONDS) //Настраиваем таймауты для медленного интернета
                .readTimeout(NETWORKTIMEOUT, TimeUnit.SECONDS)
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
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
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
