package ru.dombuketa.filmslocaror

import android.app.Application
import ru.dombuketa.filmslocaror.di.DaggerIAppComponent
import ru.dombuketa.filmslocaror.di.IAppComponent
import ru.dombuketa.filmslocaror.di.modules.DatabaseModule
import ru.dombuketa.filmslocaror.di.modules.DomainModule
import ru.dombuketa.net_module.DaggerIRemoteComponent
import ru.dombuketa.net_module.RemoteModule

class App : Application() {
    lateinit var dagger: IAppComponent
    override fun onCreate() {
        super.onCreate()
        //Инициализируем экземпляр App, через который будем получать доступ к остальным переменным
        instance = this
        val remoteProvider = DaggerIRemoteComponent.create()
        dagger = DaggerIAppComponent.builder()
            //.remoteModule(ru.dombuketa.net_module.RemoteModule())
             .iRemoteProvider(remoteProvider)
            .databaseModule(DatabaseModule())
            .domainModule(DomainModule(this))
            .build()
    }

    companion object{
        //Здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
        //Приватный сеттер, чтобы нельзя было в эту переменную присвоить что-либо другое
        private set
        const val NETWORKTIMEOUT = 60L
    }
}