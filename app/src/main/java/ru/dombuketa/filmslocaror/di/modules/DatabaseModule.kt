package ru.dombuketa.filmslocaror.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.dombuketa.filmslocaror.data.MainRepository
import ru.dombuketa.filmslocaror.data.MainRepository_J
import ru.dombuketa.filmslocaror.data.db.DatabaseHelper
import javax.inject.Singleton

@Module
class DatabaseModule{
    @Provides
    @Singleton
    fun provideDatabaseHelper(context: Context) = DatabaseHelper(context)

    @Provides
    @Singleton
    open fun provideRepository(databaseHelper: DatabaseHelper): MainRepository {
        return MainRepository(databaseHelper)
    }
}