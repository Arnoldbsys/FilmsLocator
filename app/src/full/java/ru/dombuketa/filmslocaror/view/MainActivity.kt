package ru.dombuketa.filmslocaror.view

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ru.dombuketa.db_module.dto.Film
import ru.dombuketa.filmslocaror.R
import ru.dombuketa.filmslocaror.databinding.ActivityMainBinding
import ru.dombuketa.filmslocaror.receivers.MessageReceiver_J
import ru.dombuketa.filmslocaror.view.fragments.*

class MainActivity : AppCompatActivity() {
    //val dataBase = FilmsDataBase().getFilmsDataBase()
    private lateinit var binding: ActivityMainBinding
    private lateinit var receiver : BroadcastReceiver
    private var backPressed = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        receiver = MessageReceiver_J()
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        }
        registerReceiver(receiver, filter)

        initNavigationMenu()
        binding.lottieAnim.visibility = View.GONE

        val filmFromNotification = intent.getParcelableExtra<Film>("film")
        if (filmFromNotification != null) {
            lanunchDetailsFragment(filmFromNotification)
        } else {
            val fragmentHome = checkFragmentExistence("home")
            changeFragment(fragmentHome ?: HomeFragment(), "home")
        }


    }


    fun initNavigationMenu() {
        val snackbar = Snackbar.make(binding.mainContainer, "", Snackbar.LENGTH_SHORT)
        binding.bottomNav.setOnNavigationItemSelectedListener() {
            when (it.itemId) {
                R.id.home -> {
                    val tag = "home"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment?: HomeFragment(), tag)
                    true
                }
                R.id.favorites -> {
                    val tag = "favorites"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment?: FavoritesFragment(), tag)
                    true
                }
                R.id.watch_later -> {
                    val tag = "watch_later"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment?: SeelaterFragment(), tag)
                    true
                }
                R.id.casts -> {
                    val tag = "casts"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment?: CastsFragment(), tag)
                    true
                }
                R.id.settings -> {
                    val tag = "settings"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment?: SettingsFragment(), tag)
                    true
                }

                else -> false
            }
        }
    }

    fun lanunchDetailsFragment(film: Film){
        //Создаем "посылку"
        val bundle = Bundle()
        //Кладем наш фильм в "посылку"
        bundle.putParcelable("film",film)
        //Кладем фрагмент с деталями в перменную
        val fragment = DetailsFragment()
        //Прикрепляем нашу "посылку" к фрагменту
        fragment.arguments = bundle
        //Запускаем фрагмент
        supportFragmentManager
            .beginTransaction().replace(R.id.fragment_placeholder, fragment)
            .addToBackStack(null).commit()
    }

    private fun checkFragmentExistence(tag: String) : Fragment? = supportFragmentManager.findFragmentByTag(tag)

    private fun changeFragment(fragment: Fragment, tag: String){
        supportFragmentManager.beginTransaction().replace(R.id.fragment_placeholder,fragment, tag)
            .addToBackStack(null).commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            if (backPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed()
                finish()
            } else {
                Toast.makeText(applicationContext, R.string.alert_2clickToExit, Toast.LENGTH_SHORT)
                    .show()
            }
            backPressed = System.currentTimeMillis()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    companion object consts{
        const val TIME_INTERVAL = 2000
    }
}