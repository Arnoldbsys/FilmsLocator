package ru.dombuketa.filmslocaror.view.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.dombuketa.filmslocaror.R;
import ru.dombuketa.filmslocaror.databinding.FragmentDetailsBinding;
import ru.dombuketa.filmslocaror.domain.Film;

public class DetailsFragment_J extends Fragment {
    private FragmentDetailsBinding binding;
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_details);
        //Получаем наш фильм из переданного бандла
        Film film = getIntent().getParcelableExtra("film");

        ImageView details_poster = findViewById(R.id.details_poster);
        androidx.appcompat.widget.Toolbar details_toolbar = findViewById(R.id.details_toolbar);
        TextView details_description = findViewById(R.id.details_description);
        com.google.android.material.floatingactionbutton.FloatingActionButton details_fab =
                findViewById(R.id.details_fab);

        details_poster.setImageResource(film.getPoster());
        details_toolbar.setTitle(film.getTitle());
        details_description.setText(film.getDescription());

        details_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.main_cantainer_details),"Поделюсь с друзьями", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Film film = getArguments().getParcelable("film");

        com.google.android.material.floatingactionbutton.FloatingActionButton details_fab_favorites =
                requireActivity().findViewById(R.id.details_fab_favorites);

        if (film.isInFavorites()){
            details_fab_favorites.setImageResource(R.drawable.ic_favorite_24);
        }else{
            details_fab_favorites.setImageResource(R.drawable.ic_favorite_border_24);
        }

        details_fab_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!film.isInFavorites()){
                    details_fab_favorites.setImageResource(R.drawable.ic_favorite_24);
                    film.setInFavorites(true);
                }else{
                    details_fab_favorites.setImageResource(R.drawable.ic_favorite_border_24);
                    film.setInFavorites(false);
                }
            }
        });

        binding.detailsPoster.setImageResource(film.getPoster());
        binding.detailsToolbar.setTitle(film.getTitle());
        binding.detailsDescription.setText(film.getDescription());

        binding.detailsFabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //Указываем action с которым он запускается
                intent.setAction(Intent.ACTION_SEND);
                //Кладем данные о нашем фильме
                intent.putExtra(Intent.EXTRA_TEXT,String.format("Отправить к %s /n/n %s",film.getTitle(), film.getDescription()));
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Текст сообщения"));
            }
        });

    }


}