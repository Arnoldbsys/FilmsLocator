package ru.dombuketa.filmslocaror

import android.content.Intent
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

        val details_fab_share = requireActivity().findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.details_fab_share)
        val details_fab_favorites = requireActivity().findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.details_fab_favorites)
        details_fab_favorites.setImageResource(
            if (film.isInFavorites) R.drawable.ic_favorite_24
            else R.drawable.ic_favorite_border_24
        )


        details_toolbar.title = film.title
        details_poster.setImageResource(film.poster)
        details_description.text = film.description

        details_fab_share.setOnClickListener{
            val intent = Intent()
            //Указываем action с которым он запускается
            intent.action = Intent.ACTION_SEND
            //Кладем данные о нашем фильме
            intent.putExtra(Intent.EXTRA_TEXT,"Отправить к ${film.title} \\n\\n ${film.description}")
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent,"Текст сообщения"))
        }

        details_fab_favorites.setOnClickListener{
            if (!film.isInFavorites){
                details_fab_favorites.setImageResource(R.drawable.ic_favorite_24)
                film.isInFavorites = true
            } else {
                details_fab_favorites.setImageResource(R.drawable.ic_favorite_border_24)
                film.isInFavorites = false
            }
        }
    }

}