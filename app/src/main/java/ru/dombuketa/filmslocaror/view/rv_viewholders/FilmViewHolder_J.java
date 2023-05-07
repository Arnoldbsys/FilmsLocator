package ru.dombuketa.filmslocaror.view.rv_viewholders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import ru.dombuketa.filmslocaror.R;
//import ru.dombuketa.filmslocaror.data.ApiConstants;
import ru.dombuketa.filmslocaror.view.customview.RatingDonutView_J;
import ru.dombuketa.filmslocaror.databinding.FilmItemBinding;
import ru.dombuketa.filmslocaror.domain.Film;
import ru.dombuketa.net_module.entity.ApiConstants;

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
    public void bind(Film film){
        binding.setFilm(film);
        //binding.title.setText(film.getTitle());

        //poster.setImageResource(film.getPoster());   // без Glide
        //Указываем контейнер, в котором будет "жить" наша картинка
        Glide.with(itemView)
            .load(ApiConstants.IMAGES_URL + "w342" + film.getPoster()) // .load(film.getPoster())
            .centerCrop()
            .into(binding.poster);
        //binding.description.setText(film.getDescription());
        ratingDonut.setProgress((int) (film.getRating() * RatingDonutView_J.KOEF_FOR_PAINT));
    }


}
