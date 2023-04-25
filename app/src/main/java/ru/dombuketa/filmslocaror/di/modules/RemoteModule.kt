package ru.dombuketa.filmslocaror.di.modules

import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.dombuketa.filmslocaror.App
import ru.dombuketa.filmslocaror.BuildConfig
import ru.dombuketa.filmslocaror.data.ApiConstants
import ru.dombuketa.filmslocaror.data.ITmdbApi
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class RemoteModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        //Создаём кастомный клиент
        OkHttpClient.Builder()
        //Настраиваем таймауты для медленного интернета
        .callTimeout(App.NETWORKTIMEOUT, TimeUnit.SECONDS)
        .readTimeout(App.NETWORKTIMEOUT, TimeUnit.SECONDS)
        //Добавляем логгер
        .addInterceptor(HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BASIC
            }
        })
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        //Создаем Ретрофит
        Retrofit.Builder()
        //Указываем базовый URL из констант
        .baseUrl(ApiConstants.BASE_URL)
        //Добавляем конвертер
        .addConverterFactory(GsonConverterFactory.create())
        //Добавляем кастомный клиент
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideTmpApi(retrofit: Retrofit): ITmdbApi =
        //Создаем сам сервис с методами для запросов
        retrofit.create(ITmdbApi::class.java)

}