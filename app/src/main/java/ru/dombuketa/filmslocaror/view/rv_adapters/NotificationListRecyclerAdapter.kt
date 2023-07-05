package ru.dombuketa.filmslocaror.view.rv_adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ru.dombuketa.db_module.dto.Film
import ru.dombuketa.db_module.dto.Notification
import ru.dombuketa.filmslocaror.App
import ru.dombuketa.filmslocaror.R
import ru.dombuketa.filmslocaror.databinding.NotificationItemBinding
import ru.dombuketa.filmslocaror.view.rv_helpers.IItemTouchHelperAdapter
import ru.dombuketa.filmslocaror.view.rv_viewholders.NotificationViewHolder

//в параметр передаем слушатель, чтобы мы потом могли обрабатывать нажатия из класса Activity

class NotificationListRecyclerAdapter(private val clickListener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), IItemTouchHelperAdapter {
    //Здесь у нас хранится список элементов для RV
    val interactor = App.instance.dagger.getInteractor()

    val items = mutableListOf<Notification>()
    private var lastPosition = -1
    //В этом методе мы привязываем наш ViewHolder и передаем туда "надутую" верстку нашей нотификации
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return NotificationViewHolder(binding)
    }

    //В этом методе будет привязка полей из объекта Film к View из film_item.xml
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is NotificationViewHolder ->{
                //Вызываем метод bind(), который мы создали, и передаем туда объект
                //из нашей базы данных с указанием позиции
                holder.bind(items[position])
                //Обрабатываем нажатие на весь элемент целиком(можно сделать на отдельный элемент
                //например, картинку) и вызываем метод нашего листенера, который мы получаем из
                //конструктора адаптера
                val item_container = holder.itemView.findViewById<ConstraintLayout>(R.id.item_container)
                item_container.setOnClickListener {
                    clickListener.click(items[position])
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    //Метод для добавления объектов в наш список
    fun addItems(list: List<Notification>){
        //Сначала очищаем(если не реализовать DiffUtils)
        items.clear()
        //Добавляем
        items.addAll(list)
        //Уведомляем RV, что пришел новый список, и ему нужно заново все "привязывать"
        notifyDataSetChanged()
    }

    //Интерфейс для обработки кликов
    interface OnItemClickListener{
        fun click(notif: Notification)
    }

    override fun onItemDismiss(position: Int) {
        val film_id = items[position].filmId
        items.removeAt(position)
        interactor.deactivateNotification(film_id)
        notifyItemRemoved(position)
    }
}