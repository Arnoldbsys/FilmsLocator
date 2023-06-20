package ru.dombuketa.filmslocaror.view.rv_viewholders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.dombuketa.db_module.dto.Film
import ru.dombuketa.db_module.dto.Notification
import ru.dombuketa.filmslocaror.view.customview.RatingDonutView
import ru.dombuketa.filmslocaror.databinding.FilmItemBinding
import ru.dombuketa.filmslocaror.databinding.NotificationItemBinding


//В конструктор класс передается layout, который мы создали(film_item.xml)
//class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
class NotificationViewHolder(var binding: NotificationItemBinding) : RecyclerView.ViewHolder(binding.root) {
    //Привязываем View из layout к переменным
    //В этом методе кладем данные из Film в наши View
    fun bind(notif: Notification){
        binding.notification = notif
        Glide.with(itemView)
            .load(ru.dombuketa.net_module.entity.ApiConstants.IMAGES_URL + "w342/" + notif.poster)
            .centerCrop()
            .into(binding.poster)
    }
}

