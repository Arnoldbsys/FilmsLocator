package ru.dombuketa.filmslocaror;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment_J extends Fragment {
    public List<Film> filmsDataBase = new ArrayList<Film>();
    private FilmListRecyclerAdapter_J filmsAdapter;

    public HomeFragment_J() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDatabase();
        initRV();
    }

    void initDatabase(){
        filmsDataBase = new ArrayList<Film>();
        filmsDataBase.add(new Film(1,"Титаник",R.drawable.poster_1,"Очень хороший фильм про любовь, но я его не смотрел."));
        filmsDataBase.add(new Film(2,"Тар",R.drawable.poster_2,"Не знаю, про что фильм, но по картинке похоже, что глубокомысленные, тоже не смотрел."));
        filmsDataBase.add(new Film(3,"ГудВилХантер",R.drawable.poster_3,"Кто-то кого-то искал и нашел, а может и не нашел, можно посмотреть если делать нечего."));
        filmsDataBase.add(new Film(4,"Шининг",R.drawable.poster_4,"Фильм про мальчика на велосипеде и двух сестер в коридоре. Затронута тема безопасности езды в помещениях. Неплохой фильм для расширения кругозора."));
        filmsDataBase.add(new Film(5,"Завтрак клуб",R.drawable.poster_5,"Фильм про то, что нужно брать с собой на природу чтобы не выглядеть глупо перед сверстниками во время приема пищи. Старый добрый фильм про взаимовыручку и жадность."));
        filmsDataBase.add(new Film(6,"Криминальное чтиво",R.drawable.poster_01,"Все уже хорошо знают эти четыре знаменитых сюжета, где тесно сплетаются судьбы незнакомых людей."));
        filmsDataBase.add(new Film(7,"Звездные войны",R.drawable.poster_02,"2014 год. Дарт Вэйдер хочет встать во главе Космической Федерации, но на пути у него встает с бластером ловкий Люк с Чабукой. Захватывающий фильм про будущее."));
        filmsDataBase.add(new Film(8,"Е.Т.",R.drawable.poster_03,"Приключения велосипедиста на луне. Молодой человек после вечеринки обнаруживает себя на велосипеде на луне. Фильм, пропагандирующий трезвый образ жизни. Обязателен к просмотру молодежи. Сам не смотрел."));
        filmsDataBase.add(new Film(9,"Назад в будующее",R.drawable.poster_04,"Гениальный и неженатый профессор изобрел машину времени, которая помогла ему в итоге найти спутницу жизни при помощи паровоза."));


    }

    void initRV(){
        RecyclerView main_recycler = requireActivity().findViewById(R.id.main_recycler);
        //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
        filmsAdapter = new FilmListRecyclerAdapter_J(new FilmListRecyclerAdapter_J.OnItemClickListener1() {
            @Override
            public void click(Film film) {
                ((MainActivity_J)requireActivity()).launchDetailsFragment(film);
                /*
                //Создаем бандл и кладем туда объект с данными фильма
                Bundle bundle = new Bundle();
                //Первым параметром указывается ключ, по которому потом будем искать, вторым сам
                //передаваемый объект
                bundle.putParcelable("film",film);
                Intent intent = new Intent(getApplicationContext(), DetailsFragment_J.class);
                //Прикрепляем бандл к интенту
                intent.putExtras(bundle);
                //Запускаем наше активити
                startActivity(intent);*/
            }
        });
        main_recycler.setAdapter(filmsAdapter); //Присваиваем адаптер
        //Присвои layoutmanager
        main_recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        //Применяем декоратор для отступов
        main_recycler.addItemDecoration(new TopSpacingItemDecoration_J(8));
        //Кладем нашу БД в RV
        filmsAdapter.addItems(filmsDataBase);
    }

}