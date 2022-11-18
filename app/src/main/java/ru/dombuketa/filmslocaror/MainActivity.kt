package ru.dombuketa.filmslocaror

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initMenuButtons()

    }

    fun initMenuButtons(){
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

    }
}