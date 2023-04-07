package ru.dombuketa.filmslocaror.di.modules;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.dombuketa.filmslocaror.data.MainRepository_J;
import ru.dombuketa.filmslocaror.data.dao.IFilmDao_J;
import ru.dombuketa.filmslocaror.data.db.AppDatabase_J;
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
    IFilmDao_J provideFilmDao(Context context){
        return Room.databaseBuilder(context, AppDatabase_J.class, "film_db_j").build().filmDao_J();
    }


    @Provides
    @Singleton
    MainRepository_J provideRepository(IFilmDao_J filmDao_j, DatabaseHelper_J databaseHelper_j){
        return new MainRepository_J(filmDao_j, databaseHelper_j);
    }
}
