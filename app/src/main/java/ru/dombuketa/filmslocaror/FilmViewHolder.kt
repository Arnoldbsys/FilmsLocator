package ru.dombuketa.filmslocaror

import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.dombuketa.filmslocaror.databinding.FilmItemBinding


//В конструктор класс передается layout, который мы создали(film_item.xml)
//class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
class FilmViewHolder(var binding: FilmItemBinding) : RecyclerView.ViewHolder(binding.root) {
    //Привязываем View из layout к переменным
    //В этом методе кладем данные из Film в наши View
    fun bind(film: Film){
        binding.title.text = film.title
        Glide.with(itemView)
            .load(film.poster)
            .centerCrop()
            .into(binding.poster)
        binding.description.text = film.description
        //Устанавливаем рэйтинг
        binding.ratingDonut.setProgress((film.rating * RatingDonutView.KOEF_FOR_PAINT).toInt())
    }
}

