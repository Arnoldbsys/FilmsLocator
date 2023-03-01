package ru.dombuketa.filmslocaror.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.dombuketa.filmslocaror.data.MainRepository_J;

@Module
public class DatabaseModule_J {
    @Provides
    @Singleton
    MainRepository_J provideRepository(){
        return new MainRepository_J();
    }
}
