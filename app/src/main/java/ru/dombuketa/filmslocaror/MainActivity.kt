package ru.dombuketa.filmslocaror

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initTopBar()
        initNavigationMenu()

    }
    fun initTopBar(){
        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        topAppBar.setOnMenuItemClickListener{
            when (it.itemId){
                R.id.settings ->{
                    if (AppCompatDelegate.getDefaultNightMode () != AppCompatDelegate.MODE_NIGHT_YES) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        Toast.makeText(this, "Ночной режим", Toast.LENGTH_SHORT).show()
                        it.setIcon(ContextCompat.getDrawable(applicationContext,R.drawable.ic_mode_night))
                    }
                    else
                    {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        Toast.makeText(this, "Дневной режим", Toast.LENGTH_SHORT).show()
                        it.setIcon(ContextCompat.getDrawable(applicationContext,R.drawable.ic_mode_light))
                    }
//
                    true
                }
                else -> false
            }
        }

    }
    fun initNavigationMenu(){
        val bottomNavMenu = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavMenu.setOnNavigationItemSelectedListener() {
            when(it.itemId){
                R.id.favorites->{
                    Toast.makeText(this, "Избранное", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.watch_later->{
                    Toast.makeText(this, "Посмотреть позже", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.casts->{
                    Toast.makeText(this, "Подборки", Toast.LENGTH_SHORT).show()
                    true
                }
                else->false
            }
        }
    }

    /*fun initMenuButtons(){
        val btn_menu = findViewById<Button>(R.id.button_menu)
        val btn_favs = findViewById<Button>(R.id.button_favorits)
        val btn_late = findViewById<Button>(R.id.button_seelater)
        val btn_cast = findViewById<Button>(R.id.button_casts)
        val btn_sets = findViewById<Button>(R.id.button_settings)
        btn_menu.setOnClickListener{
            Toast.makeText(this, R.string.btn_menu,Toast.LENGTH_SHORT).show()
        }
        btn_favs.setOnClickListener{
            Toast.makeText(this, R.string.btn_favorits,Toast.LENGTH_SHORT).show()
        }
        btn_late.setOnClickListener{
            Toast.makeText(this, R.string.btn_seelater,Toast.LENGTH_SHORT).show()
        }
        btn_cast.setOnClickListener{
            Toast.makeText(this, R.string.btn_casts,Toast.LENGTH_SHORT).show()
        }
        btn_sets.setOnClickListener{
            Toast.makeText(this, R.string.btn_settings,Toast.LENGTH_SHORT).show()
        }

    }*/
}