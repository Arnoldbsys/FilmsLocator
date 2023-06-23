package ru.dombuketa.filmslocaror.view.rv_viewholders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import ru.dombuketa.db_module.dto.Notification;
import ru.dombuketa.filmslocaror.databinding.NotificationItemBinding;
import ru.dombuketa.net_module.entity.ApiConstants;

public class NotificationViewHolder_J extends RecyclerView.ViewHolder {
    private NotificationItemBinding binding;

    public NotificationViewHolder_J(@NonNull NotificationItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    public void bind(Notification notif){
        binding.setNotification(notif);
        Glide.with(itemView)
                .load(ApiConstants.IMAGES_URL + "w342/" + notif.getPoster())
                .centerCrop()
                .into(binding.poster);
    }
}
