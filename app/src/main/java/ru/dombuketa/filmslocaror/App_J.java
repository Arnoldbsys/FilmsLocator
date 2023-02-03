package ru.dombuketa.filmslocaror;

import android.app.Application;

import ru.dombuketa.filmslocaror.data.MainRepository_J;
import ru.dombuketa.filmslocaror.domain.Interactor_J;

public class App_J extends Application {
    private static App_J instance;
    public static App_J getInstance() {
        return instance;
    }

    public MainRepository_J repo;
    public Interactor_J interactor;

    @Override
    public void onCreate() {
        super.onCreate();
        //Инициализируем экземпляр App, через который будем получать доступ к остальным переменным
        instance = this;
        //Инициализируем репозиторий
        repo = new MainRepository_J();
        //Инициализируем интерактор
        interactor = new Interactor_J(repo);
    }
}
