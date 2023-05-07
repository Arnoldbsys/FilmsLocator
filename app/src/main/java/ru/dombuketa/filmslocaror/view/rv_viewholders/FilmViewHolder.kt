package ru.dombuketa.filmslocaror.view.rv_viewholders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.dombuketa.net_module.entity.ApiConstants
import ru.dombuketa.filmslocaror.view.customview.RatingDonutView
import ru.dombuketa.filmslocaror.databinding.FilmItemBinding
import ru.dombuketa.filmslocaror.domain.Film


//В конструктор класс передается layout, который мы создали(film_item.xml)
//class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
class FilmViewHolder(var binding: FilmItemBinding) : RecyclerView.ViewHolder(binding.root) {
    //Привязываем View из layout к переменным
    //В этом методе кладем данные из Film в наши View
    fun bind(film: Film){
        binding.film = film
        Glide.with(itemView)
            .load(ru.dombuketa.net_module.entity.ApiConstants.IMAGES_URL + "w342/" + film.poster)
            .centerCrop()
            .into(binding.poster)
        //Устанавливаем рэйтинг
        binding.ratingDonut.setProgress((film.rating * RatingDonutView.KOEF_FOR_PAINT).toInt())
    }
}

