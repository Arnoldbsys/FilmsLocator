package ru.dombuketa.filmslocaror

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

class DetailsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val film = arguments?.get("film") as Film
        val details_toolbar = requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.details_toolbar)
        val details_poster = requireActivity().findViewById<AppCompatImageView>(R.id.details_poster)
        val details_description = requireActivity().findViewById<TextView>(R.id.details_description)
        val details_fab = requireActivity().findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.details_fab)

        details_toolbar.title = film.title
        details_poster.setImageResource(film.poster)
        details_description.text = film.description

        details_fab.setOnClickListener{
            val snackbar =
                Snackbar.make(details_description, R.string.alert_tellToFriends, Snackbar.LENGTH_SHORT)
            snackbar.show()
        }
    }

}