package ru.dombuketa.filmslocaror;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FavoritesFragment_J extends Fragment {
    private FilmListRecyclerAdapter_J filmsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Получаем список при транзакции фрагмента
        initRV();
    }



    void initRV(){

        RecyclerView favorite_rv = requireActivity().findViewById(R.id.favorites_recycler);
        //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
        filmsAdapter = new FilmListRecyclerAdapter_J(new FilmListRecyclerAdapter_J.OnItemClickListener1() {
            @Override
            public void click(Film film) {
                ((MainActivity_J)requireActivity()).launchDetailsFragment(film);
            }
        });
        favorite_rv.setAdapter(filmsAdapter); //Присваиваем адаптер
        //Присвои layoutmanager
        favorite_rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        //Применяем декоратор для отступов
        favorite_rv.addItemDecoration(new TopSpacingItemDecoration_J(8));
        //Кладем нашу БД в RV
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            filmsAdapter.addItems(((MainActivity_J)requireActivity()).dataBase.stream()
                    .filter(p->p.isInFavorites() == true).collect(Collectors.toList()));
        }
    }
}
