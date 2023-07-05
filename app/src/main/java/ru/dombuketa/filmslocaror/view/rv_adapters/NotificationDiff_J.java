package ru.dombuketa.filmslocaror.view.rv_adapters;

import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;

import ru.dombuketa.db_module.dto.Film;
import ru.dombuketa.db_module.dto.Notification;

public class NotificationDiff_J extends DiffUtil.Callback {
    ArrayList<Notification> oldList;
    ArrayList<Notification> newList;

    public NotificationDiff_J(ArrayList<Notification> oldList, ArrayList<Notification> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Notification oldNotification = oldList.get(oldItemPosition);
        Notification newNotification = newList.get(newItemPosition);
        return oldNotification.getTitle() == newNotification.getTitle() &&
                oldNotification.getStartYear() == newNotification.getStartYear() &&
                oldNotification.getStartMonth() == newNotification.getStartMonth() &&
                oldNotification.getStartDay() == newNotification.getStartDay() &&
                oldNotification.getStartHour() == newNotification.getStartHour() &&
                oldNotification.getStartMinute() == newNotification.getStartMinute();
    }
}
