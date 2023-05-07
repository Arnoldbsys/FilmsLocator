package ru.dombuketa.filmslocaror.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.dombuketa.net_module.ITmdbApi_J;
import ru.dombuketa.filmslocaror.data.MainRepository_J;
import ru.dombuketa.filmslocaror.data.PreferenceProvider_J;
import ru.dombuketa.filmslocaror.domain.Interactor_J;

@Module
//Передаем контекст для SharedPreferences через конструктор
public class DomainModule_J {
    private Context context;

    public DomainModule_J(Context context) {
        this.context = context;
    }

    //Нам нужно контекст как-то провайдить, поэтому создаем такой метод
    @Provides
    Context provideContext(){
        return context;
    }

    @Singleton
    @Provides
    PreferenceProvider_J providePreferences(Context context){
        return new PreferenceProvider_J(context);
    }

    @Singleton
    @Provides
    Interactor_J provideInteractor(MainRepository_J repository, ITmdbApi_J tmdbApi, PreferenceProvider_J prefs){
        return new Interactor_J(repository, tmdbApi, prefs);
    }
}
