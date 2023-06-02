package ru.dombuketa.db_module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.dombuketa.db_module.db.FilmDatabase
import ru.dombuketa.db_module.db.DatabaseHelper
import ru.dombuketa.db_module.api.IFilmDao
import ru.dombuketa.db_module.repos.MainRepository
import javax.inject.Singleton

private const val DATABASE_NAME = "film_db"
@Module
class DatabaseModule{

    @Provides
    @Singleton
    fun provideDatabaseHelper(context: Context) = DatabaseHelper(context)

    @Provides
    @Singleton
    fun provideFilmDao(context: Context) = Room.databaseBuilder(
        context, FilmDatabase::class.java, DATABASE_NAME
    ).build().filmDao()

    @Provides
    @Singleton
    open fun provideRepository(filmDao: IFilmDao, databaseHelper: DatabaseHelper): MainRepository {
        return MainRepository(filmDao, databaseHelper)
    }
}