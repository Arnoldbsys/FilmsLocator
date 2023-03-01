package ru.dombuketa.filmslocaror.di.modules

import dagger.Module
import dagger.Provides
import ru.dombuketa.filmslocaror.data.MainRepository
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideRepository() = MainRepository()
}