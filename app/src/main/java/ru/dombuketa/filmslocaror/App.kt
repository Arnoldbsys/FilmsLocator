package ru.dombuketa.filmslocaror

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import ru.dombuketa.db_module.DaggerIDatabaseComponent
import ru.dombuketa.db_module.api.IAppProvider
import ru.dombuketa.filmslocaror.di.DaggerIAppComponent
import ru.dombuketa.filmslocaror.di.IAppComponent
import ru.dombuketa.filmslocaror.di.modules.DomainModule
import ru.dombuketa.filmslocaror.view.notify.NotifyConsts.CHANNEL_ID
import ru.dombuketa.net_module.DaggerIRemoteComponent
import java.util.Date

class App : Application(), IAppProvider
{
    lateinit var dagger: IAppComponent
    var isPromoShow = false
    override fun onCreate() {
        super.onCreate()
        //Инициализируем экземпляр App, через который будем получать доступ к остальным переменным
        instance = this
        val remoteProvider = DaggerIRemoteComponent.create()
        val databaseProvider = DaggerIDatabaseComponent.builder().iAppProvider(provideContext() as IAppProvider).build() // iAppProvider(this)
        dagger = DaggerIAppComponent.builder()
            .domainModule(DomainModule(this))

            //.remoteModule(ru.dombuketa.net_module.RemoteModule())
            .iRemoteProvider(remoteProvider)
            //.databaseModule(DatabaseModule())
            .iDatabaseProvider(databaseProvider)
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initWatchLaterChannel()
        }
        dagger.getInteractor().setStartAppTimeToPreferences(Date().time)  //52*
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initWatchLaterChannel(){
        val name = "WatchLaterChannel"
        val desc = "FilmsLocator notification channel"
        val important = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(CHANNEL_ID, name, important)
        mChannel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }

    companion object{
        //Здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
        //Приватный сеттер, чтобы нельзя было в эту переменную присвоить что-либо другое
        private set
        const val NETWORKTIMEOUT = 60L
    }

    override fun provideContext(): Context {
        return this
    }
}