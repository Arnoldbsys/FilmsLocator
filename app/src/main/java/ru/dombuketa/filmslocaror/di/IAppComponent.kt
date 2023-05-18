package ru.dombuketa.filmslocaror.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.Provides
import ru.dombuketa.db_module.api.IAppProvider
import ru.dombuketa.db_module.api.IDatabaseProvider
//import ru.dombuketa.db_module.api.IAppProvider
import ru.dombuketa.filmslocaror.di.modules.DomainModule
import ru.dombuketa.filmslocaror.view.fragments.HomeFragment
import ru.dombuketa.filmslocaror.viewmodel.HomeFragmentViewModel
import ru.dombuketa.filmslocaror.viewmodel.SettingsFragmentViewModel
import ru.dombuketa.net_module.IRemoteProvider
import javax.inject.Singleton

@Singleton
@Component(dependencies = [IRemoteProvider::class, IDatabaseProvider::class], modules = [DomainModule::class])
//@Component(dependencies = [IRemoteProvider::class, IDatabaseProvider::class], modules = [DomainModule::class])
interface IAppComponent : IAppProvider {
    //метод для того, чтобы появилась внедрять зависимости в HomeFragmentViewModel
    fun injectt(homeFragmentViewModel: HomeFragmentViewModel)
    fun injectt(homeFragment: HomeFragment)

    //метод для того, чтобы появилась возможность внедрять зависимости в SettingsFragmentViewModel
    fun injectt(settingsFragmentViewModel: SettingsFragmentViewModel)


    //override fun provideContext(): Context



//    @Component.Factory
//    interface AppComponentFactory{
//        fun create(@BindsInstance context: Context): IAppComponent
//    }
}