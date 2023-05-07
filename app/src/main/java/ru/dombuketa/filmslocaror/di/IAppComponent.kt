package ru.dombuketa.filmslocaror.di

import dagger.Component
import ru.dombuketa.filmslocaror.di.modules.DatabaseModule
import ru.dombuketa.filmslocaror.di.modules.DomainModule
import ru.dombuketa.filmslocaror.view.fragments.HomeFragment
import ru.dombuketa.filmslocaror.viewmodel.HomeFragmentViewModel
import ru.dombuketa.filmslocaror.viewmodel.SettingsFragmentViewModel
import ru.dombuketa.net_module.IRemoteProvider
import javax.inject.Singleton

@Singleton
@Component(dependencies = [IRemoteProvider::class], modules = [DatabaseModule::class, DomainModule::class])
interface IAppComponent {
    //метод для того, чтобы появилась внедрять зависимости в HomeFragmentViewModel
    fun injectt(homeFragmentViewModel: HomeFragmentViewModel)
    fun injectt(homeFragment: HomeFragment)

    //метод для того, чтобы появилась возможность внедрять зависимости в SettingsFragmentViewModel
    fun injectt(settingsFragmentViewModel: SettingsFragmentViewModel)
}