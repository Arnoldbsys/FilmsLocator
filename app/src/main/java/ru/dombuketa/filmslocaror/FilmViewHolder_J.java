package ru.dombuketa.filmslocaror;

import android.animation.Animator;
import android.content.res.Resources;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import ru.dombuketa.filmslocaror.databinding.FilmItemBinding;

//В конструктор класс передается layout, который мы создали(film_item.xml)
public class FilmViewHolder_J extends RecyclerView.ViewHolder {
    private FilmItemBinding binding;
    public FilmViewHolder_J(@NonNull FilmItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
//    public FilmViewHolder_J(@NonNull View itemView) {
//        super(itemView);
//    }
    //Привязываем View из layout к переменным
    //private TextView title = itemView.findViewById(R.id.title);
    //ImageView poster = itemView.findViewById(R.id.poster);
    //TextView description = itemView.findViewById(R.id.description);
    private RatingDonutView_J ratingDonut = itemView.findViewById(R.id.rating_donut);
    //В этом методе кладем данные из Film в наши View
    void bind(Film film){
        binding.title.setText(film.getTitle());

        //poster.setImageResource(film.getPoster());   // без Glide
        //Указываем контейнер, в котором будет "жить" наша картинка
        Glide.with(itemView)
            .load(film.getPoster())
            .centerCrop()
            .into(binding.poster);
        binding.description.setText(film.getDescription());
        ratingDonut.setProgress((int) (film.getRating() * RatingDonutView_J.KOEF_FOR_PAINT));
    }


}
