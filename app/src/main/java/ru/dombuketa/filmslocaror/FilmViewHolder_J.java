package ru.dombuketa.filmslocaror;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//В конструктор класс передается layout, который мы создали(film_item.xml)
public class FilmViewHolder_J extends RecyclerView.ViewHolder {
    public FilmViewHolder_J(@NonNull View itemView) {
        super(itemView);
    }
    //Привязываем View из layout к переменным
    TextView title = itemView.findViewById(R.id.title);
    ImageView poster = itemView.findViewById(R.id.poster);
    TextView description = itemView.findViewById(R.id.description);

    //В этом методе кладем данные из Film в наши View
    void bind(Film film){
        title.setText(film.getTitle());
        poster.setImageResource(film.getPoster());
        description.setText(film.getDescription());
    }
}
