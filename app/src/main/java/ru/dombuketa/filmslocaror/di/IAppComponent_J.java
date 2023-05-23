package ru.dombuketa.filmslocaror.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.dombuketa.db_module.DatabaseModule_J;
import ru.dombuketa.db_module.api.IDatabaseProvider;
import ru.dombuketa.db_module.api.IDatabaseProvider_J;
import ru.dombuketa.filmslocaror.di.modules.DomainModule_J;
import ru.dombuketa.net_module.IRemoteProvider_J;
import ru.dombuketa.filmslocaror.view.fragments.HomeFragment_J;
import ru.dombuketa.filmslocaror.viewmodel.HomeFragmentViewModel_J;
import ru.dombuketa.filmslocaror.viewmodel.SettingsFragmentViewModel_J;

@Singleton
@Component(dependencies = {IRemoteProvider_J.class, IDatabaseProvider_J.class}, modules = {DomainModule_J.class})
public interface IAppComponent_J {
    //метод для того, чтобы появилась внедрять зависимости в HomeFragmentViewModel
    public void injectj(HomeFragmentViewModel_J homeFragmentViewModel_j);
    public void injectj(HomeFragment_J homeFragment_j);
    //метод для того, чтобы появилась возможность внедрять зависимости в SettingsFragmentViewModel
    public void injectj(SettingsFragmentViewModel_J settingsFragmentViewModel_j);

}
