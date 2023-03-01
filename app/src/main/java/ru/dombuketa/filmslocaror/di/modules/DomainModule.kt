package ru.dombuketa.filmslocaror.di.modules

import dagger.Module
import dagger.Provides
import ru.dombuketa.filmslocaror.data.ITmdbApi
import ru.dombuketa.filmslocaror.data.MainRepository
import ru.dombuketa.filmslocaror.domain.Interactor
import javax.inject.Singleton

@Module
class DomainModule {
    @Singleton
    @Provides
    fun provideInteractor(repository: MainRepository, tmdbApi: ITmdbApi) =
        Interactor(repo = repository, retrofitService = tmdbApi)
}