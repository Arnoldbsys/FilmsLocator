package ru.dombuketa.filmslocaror.di.modules

import android.content.Context
import android.provider.DocumentsContract.Root
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.dombuketa.filmslocaror.data.MainRepository
import ru.dombuketa.filmslocaror.data.MainRepository_J
import ru.dombuketa.filmslocaror.data.dao.IFilmDao
import ru.dombuketa.filmslocaror.data.db.AppDatabase
import ru.dombuketa.filmslocaror.data.db.DatabaseHelper
import javax.inject.Singleton

@Module
class DatabaseModule{
    @Provides
    @Singleton
    fun provideDatabaseHelper(context: Context) = DatabaseHelper(context)

    @Provides
    @Singleton
    fun provideFilmDao(context: Context) = Room.databaseBuilder(
        context, AppDatabase::class.java, "film_db"
    ).build().filmDao()


    @Provides
    @Singleton
    open fun provideRepository(filmDao: IFilmDao, databaseHelper: DatabaseHelper): MainRepository {
        return MainRepository(filmDao, databaseHelper)
    }
}