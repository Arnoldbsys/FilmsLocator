package ru.dombuketa.filmslocaror.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.dombuketa.filmslocaror.di.modules.DatabaseModule_J;
import ru.dombuketa.filmslocaror.di.modules.DomainModule_J;
import ru.dombuketa.filmslocaror.di.modules.RemoteModule_J;
import ru.dombuketa.filmslocaror.view.fragments.HomeFragment_J;
import ru.dombuketa.filmslocaror.viewmodel.HomeFragmentViewModel_J;

@Singleton
@Component(modules = {DatabaseModule_J.class, DomainModule_J.class, RemoteModule_J.class})
public interface IAppComponent_J {
    //метод для того, чтобы появилась внедрять зависимости в HomeFragmentViewModel
    void injectj(HomeFragmentViewModel_J homeFragmentViewModel_j);
    void injectj(HomeFragment_J homeFragment_j);
}
