package ru.dombuketa.filmslocaror;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FilmListRecyclerAdapter_J extends RecyclerView.Adapter {
    //Здесь у нас хранится список элементов для RV
    private ArrayList<Film> items = new ArrayList<Film>();
    private int lastPosition = -1;

    //в параметр передаем слушатель, чтобы мы потом могли обрабатывать нажатия из класса Activity
    private OnItemClickListener1 onItemClickListener;
    public FilmListRecyclerAdapter_J(OnItemClickListener1 onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //В этом методе мы привязываем наш ViewHolder и передаем туда "надутую" верстку нашего фильма
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FilmViewHolder_J(LayoutInflater.from(parent.getContext()).inflate(R.layout.film_item,parent,false));
    }
    //В этом методе будет привязка полей из объекта Film к View из film_item.xml
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //Проверяем какой у нас ViewHolder
        if (holder instanceof FilmViewHolder_J){
            //Вызываем метод bind(), который мы создали, и передаем туда объект
            //из нашей базы данных с указанием позиции
            ((FilmViewHolder_J) holder).bind(items.get(position));
            //Обрабатываем нажатие на весь элемент целиком(можно сделать на отдельный элемент
            //например, картинку) и вызываем метод нашего листенера, который мы получаем из
            //конструктора адаптера
            holder.itemView.findViewById(R.id.item_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.click(items.get(position));
                }
            });
  //          setAnimation(holder.itemView.findViewById(R.id.rating_donut), position);
        }
    }

//    private void setAnimation(View viewToAnimate, int position)
//    {
//        // If the bound view wasn't previously displayed on screen, it's animated
//        if (position > lastPosition)
//        {
//            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.rating_animator);
//            viewToAnimate.startAnimation(animation);
//            lastPosition = position;
//        }
//    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    //Метод для добавления объектов в наш список
    void addItems(List<Film> list){
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

    //Интерфейс для обработки кликов
    interface OnItemClickListener1 {
        void click(Film film);
    }
}
