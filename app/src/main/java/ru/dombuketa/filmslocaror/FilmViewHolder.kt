package ru.dombuketa.filmslocaror

import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


//В конструктор класс передается layout, который мы создали(film_item.xml)
class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    //Привязываем View из layout к переменным
    private val title = itemView.findViewById<TextView>(R.id.title)
    private val poster = itemView.findViewById<ImageView>(R.id.poster)
    private val description = itemView.findViewById<TextView>(R.id.description)
    //Вот здесь мы находим в верстке наш прогресс бар для рейтинга
    private val ratingDonut = itemView.findViewById<RatingDonutView>(R.id.rating_donut)
    //В этом методе кладем данные из Film в наши View
    fun bind(film: Film){
        title.text = film.title
        Glide.with(itemView)
            .load(film.poster)
            .centerCrop()
            .into(poster)
        description.text = film.description
        //Устанавливаем рэйтинг
        ratingDonut.setProgress((film.rating * RatingDonutView.KOEF_FOR_PAINT).toInt())
    }
}

