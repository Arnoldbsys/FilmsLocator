package ru.dombuketa.filmslocaror;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_J extends AppCompatActivity {
    public List<Film> filmsDataBase = new ArrayList<Film>();
    private FilmListRecyclerAdapter_J filmsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTopBar();
        initNavigationMenu();
        initData();
        initRV();

    }


    void initTopBar() {
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case (R.id.settings):
                        if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            Toast.makeText(getApplicationContext(), "Ночной режим", Toast.LENGTH_SHORT).show();
                            item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_mode_night));
                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            Toast.makeText(getApplicationContext(), "Дневной режим", Toast.LENGTH_SHORT).show();
                            item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_mode_light));
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
    }


    void initNavigationMenu() {
        BottomNavigationView bottomNavMenu = findViewById(R.id.bottom_nav);
        //ConstraintLayout main_cantainer = findViewById(R.id.main_container);

        bottomNavMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                RecyclerView rv = findViewById(R.id.main_recycler);
                Snackbar snackbar = Snackbar.make(rv,"", Snackbar.LENGTH_SHORT);

                switch (item.getItemId()){
                    case (R.id.favorites):
                        snackbar.setText("Избранное");
                        snackbar.show();
                        return true;
                    case (R.id.watch_later):
                        snackbar.setText( "Посмотреть позже");
                        snackbar.show();
                        return true;
                    case (R.id.casts):
                        Toast.makeText(getApplicationContext(), "Подборки", Toast.LENGTH_LONG).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    void initData(){
        filmsDataBase.add(new Film("Титаник",R.drawable.poster_1,"Очень хороший фильм про любовь, но я его не смотрел."));
        filmsDataBase.add(new Film("Тар",R.drawable.poster_2,"Не знаю, про что фильм, но по картинке похоже, что глубокомысленные, тоже не смотрел."));
        filmsDataBase.add(new Film("ГудВилХантер",R.drawable.poster_3,"Кто-то кого-то искал и нашел, а может и не нашел, можно посмотреть если делать нечего."));
        filmsDataBase.add(new Film("Шининг",R.drawable.poster_4,"Фильм про мальчика на велосипеде и двух сестер в коридоре. Затронута тема безопасности езды в помещениях. Неплохой фильм для расширения кругозора."));
        filmsDataBase.add(new Film("Завтрак клуб",R.drawable.poster_5,"Фильм про то, что нужно брать с собой на природу чтобы не выглядеть глупо перед сверстниками во время приема пищи. Старый добрый фильм про взаимовыручку и жадность."));
        filmsDataBase.add(new Film("Криминальное чтиво",R.drawable.poster_01,"Все уже хорошо знают эти четыре знаменитых сюжета, где тесно сплетаются судьбы незнакомых людей."));
        filmsDataBase.add(new Film("Звездные войны",R.drawable.poster_02,"2014 год. Дарт Вэйдер хочет встать во главе Космической Федерации, но на пути у него встает с бластером ловкий Люк с Чабукой. Захватывающий фильм про будущее."));
        filmsDataBase.add(new Film("Е.Т.",R.drawable.poster_03,"Приключения велосипедиста на луне. Молодой человек после вечеринки обнаруживает себя на велосипеде на луне. Фильм, пропагандирующий трезвый образ жизни. Обязателен к просмотру молодежи. Сам не смотрел."));
        filmsDataBase.add(new Film("Назад в будующее",R.drawable.poster_04,"Гениальный и неженатый профессор изобрел машину времени, которая помогла ему в итоге найти спутницу жизни при помощи паровоза."));



    }

    void initRV(){
        RecyclerView main_recycler = findViewById(R.id.main_recycler);
        //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
        filmsAdapter = new FilmListRecyclerAdapter_J(new FilmListRecyclerAdapter_J.OnItemClickListener1() {
            @Override
            public void click(Film film) {
                //Создаем бандл и кладем туда объект с данными фильма
                Bundle bundle = new Bundle();
                //Первым параметром указывается ключ, по которому потом будем искать, вторым сам
                //передаваемый объект
                bundle.putParcelable("film",film);
                Intent intent = new Intent(getApplicationContext(), DetailsActivity_J.class);
                //Прикрепляем бандл к интенту
                intent.putExtras(bundle);
                //Запускаем наше активити
                startActivity(intent);
            }
        });
        main_recycler.setAdapter(filmsAdapter); //Присваиваем адаптер
        //Присвои layoutmanager
        main_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //Применяем декоратор для отступов
        main_recycler.addItemDecoration(new TopSpacingItemDecoration_J(8));
        //Кладем нашу БД в RV
        filmsAdapter.addItems(filmsDataBase);
    }

}