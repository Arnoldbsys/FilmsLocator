package ru.dombuketa.filmslocaror

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    val dataBase = FilmsDataBase().getFilmsDataBase()
    private var backPressed = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.fragment_placeholder, HomeFragment())
            .addToBackStack(null).commit()

        initNavigationMenu()

    }

    fun initNavigationMenu() {
        val bottomNavMenu = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val main_container = findViewById<ConstraintLayout>(R.id.main_container)
        val snackbar = Snackbar.make(main_container, "", Snackbar.LENGTH_SHORT)
        bottomNavMenu.setOnNavigationItemSelectedListener() {
            when (it.itemId) {
                R.id.favorites -> {
                    if (supportFragmentManager.findFragmentById(R.id.fragment_placeholder) is FavoritesFragment)
                        true
                    else
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_placeholder,FavoritesFragment())
                        .addToBackStack(null).commit()
                    true
                }
                R.id.watch_later -> {
                    snackbar.setText(R.string.btn_seelater)
                    snackbar.show()
                    true
                }
                R.id.casts -> {
                    Toast.makeText(this, R.string.btn_casts, Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    fun lanunchDetailsFragment(film:Film){
        //?????????????? "??????????????"
        val bundle = Bundle()
        //???????????? ?????? ?????????? ?? "??????????????"
        bundle.putParcelable("film",film)
        //???????????? ???????????????? ?? ???????????????? ?? ??????????????????
        val fragment = DetailsFragment()
        //?????????????????????? ???????? "??????????????" ?? ??????????????????
        fragment.arguments = bundle
        //?????????????????? ????????????????
        supportFragmentManager
            .beginTransaction().replace(R.id.fragment_placeholder, fragment)
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


    companion object consts{
        const val TIME_INTERVAL = 2000
    }
}