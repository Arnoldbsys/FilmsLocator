package ru.dombuketa.filmslocaror.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.dombuketa.db_module.repos.MainRepository
import ru.dombuketa.filmslocaror.data.PreferenceProvider
import ru.dombuketa.filmslocaror.domain.Interactor
import ru.dombuketa.filmslocaror.view.notify.NotifyHelper
import javax.inject.Singleton

@Module
//Передаем контекст для SharedPreferences через конструктор
class DomainModule(val context: Context) {
    //Нам нужно контекст как-то провайдить, поэтому создаем такой метод
    @Provides
    fun  provideContext() = context

    @Singleton
    @Provides
    //Создаем экземпляр SharedPreferences
    fun providePreferences(context: Context) = PreferenceProvider(context)

    @Singleton
    @Provides
    //Создаем экземпляр NotifyHelper
    fun provideNotifyHelper() = NotifyHelper()

    @Singleton
    @Provides
    fun provideInteractor(repository: MainRepository, tmdbApi: ru.dombuketa.net_module.ITmdbApi, preferenceProvider: PreferenceProvider) =
        Interactor(repo = repository, retrofitService = tmdbApi, preferences = preferenceProvider)
}