package ru.dombuketa.filmslocaror.view.rv_adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.dombuketa.db_module.dto.Notification;
import ru.dombuketa.filmslocaror.App_J;
import ru.dombuketa.filmslocaror.R;
import ru.dombuketa.filmslocaror.databinding.NotificationItemBinding;
import ru.dombuketa.filmslocaror.domain.Interactor_J;
import ru.dombuketa.filmslocaror.view.rv_helpers.IItemTouchHelperAdapter_J;
import ru.dombuketa.filmslocaror.view.rv_viewholders.NotificationViewHolder_J;

//в параметр передаем слушатель, чтобы мы потом могли обрабатывать нажатия из класса Activity
public class NotificationListRecyclerAdapter_J extends RecyclerView.Adapter implements IItemTouchHelperAdapter_J {
    private ArrayList<Notification> items = new ArrayList<Notification>();
    private OnItemClickListener clickListener;
    public NotificationListRecyclerAdapter_J(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private int lastPosition = -1;
    Interactor_J interactor_j = App_J.getInstance().daggerj.getInteractor_j();

    @NonNull
    @Override  ////В этом методе мы привязываем наш ViewHolder и передаем туда "надутую" верстку нашей нотификации
    public NotificationViewHolder_J onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificationItemBinding binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NotificationViewHolder_J(binding);
    }

    @Override  ////В этом методе будет привязка полей из объекта Film к View из film_item.xml
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Проверяем какой у нас ViewHolder
        if (holder instanceof NotificationViewHolder_J){
            //Вызываем метод bind(), который мы создали, и передаем туда объект
            //из нашей базы данных с указанием позиции
            ((NotificationViewHolder_J) holder).bind(items.get(position));
            //Обрабатываем нажатие на весь элемент целиком(можно сделать на отдельный элемент
            //например, картинку) и вызываем метод нашего листенера, который мы получаем из
            //конструктора адаптера
            holder.itemView.findViewById(R.id.item_container).setOnClickListener(view ->
                    clickListener.click(items.get(position))
            );
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onItemDismiss(int position) {
        int film_id = items.get(position).getFilmId();
        items.remove(position);
        interactor_j.deactivateNotification(film_id);
        notifyItemRemoved(position);
    }

    //Метод для добавления объектов в наш список
    public void addItems(List<Notification> list){
        //Сначала очищаем(если не реализовать DiffUtils)
        items.clear();
        //Добавляем
        items.addAll(list);
        //Уведомляем RV, что пришел новый список, и ему нужно заново все "привязывать"
        this.notifyDataSetChanged();
    }

    //Интерфейс для обработки кликов
    public interface OnItemClickListener{
        void click(Notification notif);
    }
}
