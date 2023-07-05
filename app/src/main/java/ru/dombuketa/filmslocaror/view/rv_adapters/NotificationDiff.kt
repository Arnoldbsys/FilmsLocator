package ru.dombuketa.filmslocaror.view.rv_adapters

import androidx.recyclerview.widget.DiffUtil
import ru.dombuketa.db_module.dto.Film
import ru.dombuketa.db_module.dto.Notification

class NotificationDiff(val oldList:ArrayList<Notification>, val newList: ArrayList<Notification>) :DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldNotification = oldList[oldItemPosition]
        val newNotification = newList[newItemPosition]
        return oldNotification.title == newNotification.title &&
                oldNotification.startYear == newNotification.startYear &&
                oldNotification.startMonth == newNotification.startMonth &&
                oldNotification.startDay == newNotification.startDay &&
                oldNotification.startHour == newNotification.startHour &&
                oldNotification.startMinute == newNotification.startMinute

    }
}