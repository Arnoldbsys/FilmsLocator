package ru.dombuketa.filmslocaror.di

import dagger.Component
import ru.dombuketa.filmslocaror.di.modules.DatabaseModule
import ru.dombuketa.filmslocaror.di.modules.DomainModule
import ru.dombuketa.filmslocaror.di.modules.RemoteModule
import ru.dombuketa.filmslocaror.view.fragments.HomeFragment
import ru.dombuketa.filmslocaror.viewmodel.HomeFragmentViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [RemoteModule::class, DatabaseModule::class, DomainModule::class])
interface IAppComponent {
    //метод для того, чтобы появилась внедрять зависимости в HomeFragmentViewModel
    fun injectt(homeFragmentViewModel: HomeFragmentViewModel)
    fun injectt(homeFragment: HomeFragment)
}