package ru.dombuketa.filmslocaror.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.dombuketa.filmslocaror.data.MainRepository_J;
import ru.dombuketa.filmslocaror.data.db.DatabaseHelper_J;

@Module
public class DatabaseModule_J {
    @Provides
    @Singleton
    DatabaseHelper_J provideDatabaseHelper_j(Context context){
        return new DatabaseHelper_J(context);
    }
    @Provides
    @Singleton
    MainRepository_J provideRepository(DatabaseHelper_J databaseHelper_j){
        return new MainRepository_J(databaseHelper_j);
    }
}
