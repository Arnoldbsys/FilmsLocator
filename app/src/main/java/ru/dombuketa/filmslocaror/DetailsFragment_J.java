package ru.dombuketa.filmslocaror;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class DetailsFragment_J extends Fragment {
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
        return inflater.inflate(R.layout.fragment_details, container, false); // super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Film film = getArguments().getParcelable("film");
        ImageView details_poster = requireActivity().findViewById(R.id.details_poster);
        androidx.appcompat.widget.Toolbar details_toolbar = requireActivity().findViewById(R.id.details_toolbar);
        TextView details_description = requireActivity().findViewById(R.id.details_description);
        com.google.android.material.floatingactionbutton.FloatingActionButton details_fab =
                requireActivity().findViewById(R.id.details_fab);

        details_poster.setImageResource(film.getPoster());
        details_toolbar.setTitle(film.getTitle());
        details_description.setText(film.getDescription());

        details_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(details_description,R.string.alert_tellToFriends, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
    }

}