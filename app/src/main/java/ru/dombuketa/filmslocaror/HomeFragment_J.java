package ru.dombuketa.filmslocaror;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.Fade;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class HomeFragment_J extends Fragment {
    //public List<Film> filmsDataBase = new ArrayList<Film>();
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
//        initDatabase();
        CoordinatorLayout home_fragment_root = requireActivity().findViewById(R.id.home_fragment_root);
/* предыдущая анимация м28
        Scene scene = Scene.getSceneForLayout(home_fragment_root, R.layout.fragment_home, requireContext());
        //Создаем анимацию выезда поля поиска сверху
        Slide searchSlider = (Slide) new Slide(Gravity.TOP).addTarget(R.id.search_view);
        //Создаем анимацию выезда RV снизу
        Slide recyclerSlider = (Slide) new Slide(Gravity.BOTTOM).addTarget(R.id.main_recycler);
        Fade searchFade = (Fade) new Fade(Fade.MODE_IN).addTarget(R.id.search_view);
        //Создаем экземпляр TransitionSet, который объединит все наши анимации
        TransitionSet customTransition = new TransitionSet();
        customTransition.setDuration(500);
        customTransition.addTransition(recyclerSlider);
        //customTransition.addTransition(searchSlider);
        customTransition.addTransition(searchFade);
        TransitionManager.go(scene, customTransition);
*/
        initSearchView();
        initHomeRV();
        AnimationHelper_J.performFragmentCircularRevealAnimation(home_fragment_root, requireActivity(),1);

    }

    private void initSearchView() {
        SearchView search_view = requireActivity().findViewById(R.id.search_view);
        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_view.setIconified(false);
            }
        });
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextChange(String newText) {
                //Если ввод пуст то вставляем в адаптер всю БД
                if (newText.isEmpty()){
                    filmsAdapter.addItems(((MainActivity_J)requireActivity()).dataBase);
                    return true;
                }
                //Фильтруем список на поискк подходящих сочетаний
                //Чтобы все работало правильно, нужно и запрос, и имя фильма приводить к нижнему регистру
                List<Film> result = ((MainActivity_J)(requireActivity())).dataBase.stream().filter(
                        x -> x.getTitle().toLowerCase(Locale.getDefault())
                            .contains(newText.toLowerCase(Locale.getDefault())))
                            .collect(Collectors.toList());
                filmsAdapter.addItems(result);
                return true;
            }
        });
    }

//    void initDatabase(){
//        filmsDataBase = new ArrayList<Film>();
//        filmsDataBase.add(new Film(1,"Титаник",R.drawable.poster_1,"Очень хороший фильм про любовь, но я его не смотрел."));
//        filmsDataBase.add(new Film(2,"Тар",R.drawable.poster_2,"Не знаю, про что фильм, но по картинке похоже, что глубокомысленные, тоже не смотрел."));
//        filmsDataBase.add(new Film(3,"ГудВилХантер",R.drawable.poster_3,"Кто-то кого-то искал и нашел, а может и не нашел, можно посмотреть если делать нечего."));
//        filmsDataBase.add(new Film(4,"Шининг",R.drawable.poster_4,"Фильм про мальчика на велосипеде и двух сестер в коридоре. Затронута тема безопасности езды в помещениях. Неплохой фильм для расширения кругозора."));
//        filmsDataBase.add(new Film(5,"Завтрак клуб",R.drawable.poster_5,"Фильм про то, что нужно брать с собой на природу чтобы не выглядеть глупо перед сверстниками во время приема пищи. Старый добрый фильм про взаимовыручку и жадность."));
//        filmsDataBase.add(new Film(6,"Криминальное чтиво",R.drawable.poster_01,"Все уже хорошо знают эти четыре знаменитых сюжета, где тесно сплетаются судьбы незнакомых людей."));
//        filmsDataBase.add(new Film(7,"Звездные войны",R.drawable.poster_02,"2014 год. Дарт Вэйдер хочет встать во главе Космической Федерации, но на пути у него встает с бластером ловкий Люк с Чабукой. Захватывающий фильм про будущее."));
//        filmsDataBase.add(new Film(8,"Е.Т.",R.drawable.poster_03,"Приключения велосипедиста на луне. Молодой человек после вечеринки обнаруживает себя на велосипеде на луне. Фильм, пропагандирующий трезвый образ жизни. Обязателен к просмотру молодежи. Сам не смотрел."));
//        filmsDataBase.add(new Film(9,"Назад в будующее",R.drawable.poster_04,"Гениальный и неженатый профессор изобрел машину времени, которая помогла ему в итоге найти спутницу жизни при помощи паровоза."));
//    }

    void initHomeRV(){
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
        filmsAdapter.addItems(((MainActivity_J)requireActivity()).dataBase);

        main_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                SearchView search_view = requireActivity().findViewById(R.id.search_view);
                if (dy < 0)
                    //search_view.
                    Toast.makeText(requireActivity(), "dy < 0", Toast.LENGTH_SHORT).show();
                else if (dy > 0)
                    Toast.makeText(requireActivity(), "dy > 0", Toast.LENGTH_SHORT).show();
            }
                //super.onScrolled(recyclerView, dx, dy);

        });

    }

}