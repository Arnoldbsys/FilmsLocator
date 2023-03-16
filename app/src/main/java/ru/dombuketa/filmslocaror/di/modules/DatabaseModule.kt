package ru.dombuketa.filmslocaror.di.modules

import dagger.Module
import dagger.Provides
import ru.dombuketa.filmslocaror.data.MainRepository
import ru.dombuketa.filmslocaror.data.MainRepository_J
import javax.inject.Singleton

@Module
class DatabaseModule{
//    @Binds

//    abstract fun bindRepository(mainRepository: MainRepository) : IRepository

    @Provides
    @Singleton
    open fun provideRepository(): MainRepository {
        return MainRepository()
    }
}