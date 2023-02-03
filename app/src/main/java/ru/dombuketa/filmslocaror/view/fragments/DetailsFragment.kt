package ru.dombuketa.filmslocaror.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.dombuketa.filmslocaror.R
import ru.dombuketa.filmslocaror.databinding.FragmentDetailsBinding
import ru.dombuketa.filmslocaror.domain.Film

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val film = arguments?.get("film") as Film

        binding.detailsFabFavorites.setImageResource(
            if (film.isInFavorites) R.drawable.ic_favorite_24
            else R.drawable.ic_favorite_border_24
        )
        binding.detailsFabFavorites.setOnClickListener{
            if (!film.isInFavorites){
                binding.detailsFabFavorites.setImageResource(R.drawable.ic_favorite_24)
                film.isInFavorites = true
            } else {
                binding.detailsFabFavorites.setImageResource(R.drawable.ic_favorite_border_24)
                film.isInFavorites = false
            }
        }


        binding.detailsToolbar.title = film.title
        binding.detailsPoster.setImageResource(film.poster)
        binding.detailsDescription.text = film.description

        binding.detailsFabShare.setOnClickListener{
            val intent = Intent()
            //Указываем action с которым он запускается
            intent.action = Intent.ACTION_SEND
            //Кладем данные о нашем фильме
            intent.putExtra(Intent.EXTRA_TEXT,"Отправить к ${film.title} \\n\\n ${film.description}")
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent,"Текст сообщения"))
        }

    }

}