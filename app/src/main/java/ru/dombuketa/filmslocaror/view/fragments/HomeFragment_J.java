package ru.dombuketa.filmslocaror.view.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ru.dombuketa.filmslocaror.App_J;
import ru.dombuketa.filmslocaror.data.PreferenceProvider_J;
import ru.dombuketa.filmslocaror.domain.Interactor;
import ru.dombuketa.filmslocaror.domain.Interactor_J;
import ru.dombuketa.filmslocaror.utils.AutoDisposable_J;
import ru.dombuketa.filmslocaror.view.rv_adapters.FilmListRecyclerAdapter_J;
import ru.dombuketa.filmslocaror.view.MainActivity_J;
import ru.dombuketa.filmslocaror.R;
import ru.dombuketa.filmslocaror.utils.TopSpacingItemDecoration_J;
import ru.dombuketa.filmslocaror.databinding.FragmentHomeBinding;
import ru.dombuketa.filmslocaror.domain.Film;
import ru.dombuketa.filmslocaror.utils.AnimationHelper_J;
import ru.dombuketa.filmslocaror.viewmodel.HomeFragmentViewModel_J;


public class HomeFragment_J extends Fragment {
//    @Inject public Interactor_J interactor;
    private FragmentHomeBinding binding;
    private HomeFragmentViewModel_J viewModel; //  = ViewModelProvider.NewInstanceFactory.getInstance().create(HomrFragmentViewModel_J.class);
    private static final int FILMS_ITEM_SHIFT = 4;
    private static final int FILMS_PER_PAGE = 20;
    private int pageNumber = 1;
    private int lastVisibleItem = 0;
    LinearLayoutManager layoutManager;

    private FilmListRecyclerAdapter_J filmsAdapter;
    private List<Film> filmsDataBase = new ArrayList<Film>();
    private AutoDisposable_J autoDisposable_j = new AutoDisposable_J();

    public void setFilmsDataBase(List<Film> value) {
        //Если придет такое же значение, то мы выходим из метода
        if (filmsDataBase == value) return;
        //Если пришло другое значение, то кладем его в переменную
        filmsDataBase = value;
        //Обновляем RV адаптер
        filmsAdapter.addItems(filmsDataBase);
    }

    private HomeFragmentViewModel_J getViewModel() {
        return viewModel == null ? viewModel = new ViewModelProvider(this).get(HomeFragmentViewModel_J.class) : viewModel;
    }

    public HomeFragment_J() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        App_J.getInstance().daggerj.injectj(this);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        autoDisposable_j.bindTo(getLifecycle());
//38*
        getViewModel().currentCategory.observe(getViewLifecycleOwner(), s -> {
            filmsAdapter.clearItems();
            //Делаем новый запрос фильмов на сервер
            viewModel.getFilms(1);
            Log.i("onCreateView", s);
        });
//38*_
        //41*
        getViewModel().errorNetworkConnection.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!s.isEmpty()) {
                    Toast.makeText(requireActivity(), s, Toast.LENGTH_SHORT).show();
                    getViewModel().clearErrorConnectionError();
                }
            }
        });
        //41*_
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initDatabase();
        //CoordinatorLayout home_fragment_root = requireActivity().findViewById(R.id.home_fragment_root);
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
        AnimationHelper_J.performFragmentCircularRevealAnimation(binding.homeFragmentRoot, requireActivity(),1);
        initPullToRefresh();
        autoDisposable_j.add(
            getViewModel().filmsListLiveDataRx
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    //Кладем нашу БД в RV
                    setFilmsDataBase(list);
                })
            );
        getViewModel().showProgressBar
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(sw -> {
                binding.progressBar.setVisibility(sw);
            });
        initHomeRV();
    }



    private void initPullToRefresh(){
        //Вешаем слушатель, чтобы вызвался pull to refresh
        binding.pullToRefresh.setOnRefreshListener(() -> {
            //Чистим адаптер(items нужно будет сделать паблик или создать для этого публичный метод)
            Log.i("HomeFragment","InitPullRefresh");
            filmsAdapter.clearItems();
            //Делаем новый запрос фильмов на сервер
            viewModel.getFilms(1);
            //Убираем крутящееся колечко
            binding.pullToRefresh.setRefreshing(false);
        });
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
                    filmsAdapter.addItems(filmsDataBase);
                    return true;
                }
                //Фильтруем список на поискк подходящих сочетаний
                //Чтобы все работало правильно, нужно и запрос, и имя фильма приводить к нижнему регистру
                List<Film> result = filmsDataBase.stream().filter(
                        x -> x.getTitle().toLowerCase(Locale.getDefault())
                            .contains(newText.toLowerCase(Locale.getDefault())))
                            .collect(Collectors.toList());
                filmsAdapter.addItems(result);
                return true;
            }
        });
    }



    void initHomeRV(){
        RecyclerView main_recycler = requireActivity().findViewById(R.id.main_recycler);
        //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
        filmsAdapter = new FilmListRecyclerAdapter_J(new FilmListRecyclerAdapter_J.OnItemClickListener() {
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
        layoutManager = ((LinearLayoutManager)main_recycler.getLayoutManager());

        //Применяем декоратор для отступов
        main_recycler.addItemDecoration(new TopSpacingItemDecoration_J(8));
        //Кладем нашу БД в RV
        //filmsAdapter.addItems(filmsDataBase);
        main_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && layoutManager.findLastVisibleItemPosition() > lastVisibleItem) // Прокрутка вниз
                {
                    int totalItemCount = layoutManager.getItemCount();
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                    System.out.println("!!! " + " totalItemCount=" + totalItemCount + " lastVisiblesItems=" + lastVisibleItem);

                    if (lastVisibleItem + FILMS_ITEM_SHIFT == FILMS_PER_PAGE * pageNumber - 1) {
                        viewModel.getFilms(++pageNumber);

/*
                        interactor.getFilmsFromApi(pageNumber + 1, new HomeFragmentViewModel_J.IApiCallback() {
                            @Override
                            public void onSuc() {
//                                List<Film> newfilmsDataBase = ((HomeFragmentViewModel_J)viewModel).filmsListLiveData.getValue();
//                                newfilmsDataBase.addAll(films);
//                                ((HomeFragmentViewModel_J)viewModel).filmsListLiveData.postValue(newfilmsDataBase);
//                                filmsAdapter.addItems(newfilmsDataBase);
//                                pageNumber++;
                            }
                            @Override
                            public void onFal() {

                            }
                        });
*/
                    }
                }
            }

        });

    }

}