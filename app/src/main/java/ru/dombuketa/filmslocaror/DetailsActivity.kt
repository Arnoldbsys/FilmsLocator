package ru.dombuketa.filmslocaror

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.widget.AppCompatImageView

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val film = intent.extras?.get("film") as Film
        val details_toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.details_toolbar)
        val details_poster = findViewById<AppCompatImageView>(R.id.details_poster)
        val details_description = findViewById<TextView>(R.id.details_description)

        details_toolbar.title = film.title
        details_poster.setImageResource(film.poster)
        details_description.text = film.description


    }
}