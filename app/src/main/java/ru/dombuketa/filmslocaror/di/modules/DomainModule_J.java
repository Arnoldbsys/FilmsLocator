package ru.dombuketa.filmslocaror.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.dombuketa.filmslocaror.data.ITmdbApi_J;
import ru.dombuketa.filmslocaror.data.MainRepository_J;
import ru.dombuketa.filmslocaror.domain.Interactor_J;

@Module
public class DomainModule_J {
    @Singleton
    @Provides
    Interactor_J provideInteractor(MainRepository_J repository, ITmdbApi_J tmdbApi){
        return new Interactor_J(repository, tmdbApi);
    }
}
