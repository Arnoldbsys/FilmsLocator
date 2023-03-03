package ru.dombuketa.filmslocaror.di.modules

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.dombuketa.filmslocaror.data.IRepository
import ru.dombuketa.filmslocaror.data.MainRepository
import javax.inject.Singleton

@Module
abstract class DatabaseModule{
    @Binds
    abstract fun bindRepository(mainRepository: MainRepository) : IRepository
}