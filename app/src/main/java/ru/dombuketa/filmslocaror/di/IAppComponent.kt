package ru.dombuketa.filmslocaror.di

import dagger.Component
import ru.dombuketa.db_module.api.IDatabaseProvider
import ru.dombuketa.filmslocaror.di.modules.DomainModule
import ru.dombuketa.filmslocaror.domain.Interactor
import ru.dombuketa.filmslocaror.receivers.ReminderBroadcast
import ru.dombuketa.filmslocaror.view.fragments.HomeFragment
import ru.dombuketa.filmslocaror.view.fragments.SeelaterFragment
import ru.dombuketa.filmslocaror.view.notify.NotifyHelper
import ru.dombuketa.filmslocaror.viewmodel.HomeFragmentViewModel
import ru.dombuketa.filmslocaror.viewmodel.SeelaterFragmentViewModel
import ru.dombuketa.filmslocaror.viewmodel.SettingsFragmentViewModel
import ru.dombuketa.net_module.IRemoteProvider
import javax.inject.Singleton

@Singleton
@Component(dependencies = [IRemoteProvider::class, IDatabaseProvider::class], modules = [DomainModule::class])
//@Component(dependencies = [IRemoteProvider::class, IDatabaseProvider::class], modules = [DomainModule::class])
interface IAppComponent {
    //метод для того, чтобы появилась внедрять зависимости в HomeFragmentViewModel
    fun injectt(homeFragmentViewModel: HomeFragmentViewModel)
    fun injectt(homeFragment: HomeFragment)

    //метод для того, чтобы появилась возможность внедрять зависимости в SettingsFragmentViewModel
    fun injectt(settingsFragmentViewModel: SettingsFragmentViewModel)

    fun injectt(seelaterFragmentViewModel: SeelaterFragmentViewModel)
    fun injectt(seelaterFragment: SeelaterFragment)

    fun injectt(reminderBroadcast: ReminderBroadcast)

    fun getNotifyHelper() : NotifyHelper
    fun getInteractor() : Interactor




}