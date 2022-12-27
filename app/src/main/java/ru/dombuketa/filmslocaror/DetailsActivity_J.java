package ru.dombuketa.filmslocaror;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

public class DetailsActivity_J extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //Получаем наш фильм из переданного бандла
        Film film = getIntent().getParcelableExtra("film");

        ImageView details_poster = findViewById(R.id.details_poster);
        androidx.appcompat.widget.Toolbar details_toolbar = findViewById(R.id.details_toolbar);
        TextView details_description = findViewById(R.id.details_description);

        com.google.android.material.floatingactionbutton.FloatingActionButton details_fab =
                findViewById(R.id.detail_fab);

        details_poster.setImageResource(film.getPoster());
        details_toolbar.setTitle(film.getTitle());
        details_description.setText(film.getDescription());

        details_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.main_container_details),"Поделюсь с друзьями", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

    }
}