package ru.dombuketa.db_module;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.dombuketa.db_module.db.FilmDatabase_J;
import ru.dombuketa.db_module.db.DatabaseHelper_J;
import ru.dombuketa.db_module.api.IFilmDao_J;
import ru.dombuketa.db_module.db.Migrations;
import ru.dombuketa.db_module.repos.MainRepository_J;

@Module
public class DatabaseModule_J {
    private String DATABASE_NAME = "film_db_j";
    @Provides
    @Singleton
    DatabaseHelper_J provideDatabaseHelper_j(Context context){
        return new DatabaseHelper_J(context);
    }
    @Provides
    @Singleton
    IFilmDao_J provideFilmDao(Context context){
        return Room.databaseBuilder(context, FilmDatabase_J.class, DATABASE_NAME)
                .addMigrations(Migrations.MIGRATION_1_2)
                .addMigrations(Migrations.MIGRATION_2_3)
                .addMigrations(Migrations.MIGRATION_3_4)
                .addMigrations(Migrations.MIGRATION_4_5)
                .build().filmDao();
    }

    @Provides
    @Singleton
    MainRepository_J provideRepository(IFilmDao_J filmDao_j, DatabaseHelper_J databaseHelper_j){
        return new MainRepository_J(filmDao_j, databaseHelper_j);
    }
}
