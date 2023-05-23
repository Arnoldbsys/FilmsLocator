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

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.exceptions.UndeliverableException;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ru.dombuketa.db_module.dto.Film;
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
        //initSearchView();
        initSearchViewByAPI();
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
        initRxErrorhandler();
    }



    private void initPullToRefresh(){
        //Вешаем слушатель, чтобы вызвался pull to refresh
        binding.pullToRefresh.setOnRefreshListener(() -> {
            //Сбрасываем строку поиска
            //Чистим адаптер(items нужно будет сделать паблик или создать для этого публичный метод)
            filmsAdapter.clearItems();
            String query = binding.searchView.getQuery().toString().trim();
            //Делаем новый запрос фильмов на сервер
            if (query.isEmpty() || query.trim() == ""){
                viewModel.getFilms(1);
            } else {
                viewModel.getFilmsBySearch(query, 1)
                        //.doOnError(err -> System.out.println(err.getMessage()))
                        .subscribeOn(Schedulers.io())
                        .subscribe(films -> filmsAdapter.addItems(films),
                                    err -> System.out.println(err.getMessage()));
            }
            //Убираем крутящееся колечко
            binding.pullToRefresh.setRefreshing(false);
        });
    }



    // Поиск через API запрос
    private void initSearchViewByAPI(){
        binding.searchView.setOnClickListener(cl -> binding.searchView.setIconified(false));
        Disposable data = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@io.reactivex.rxjava3.annotations.NonNull ObservableEmitter<String> emitter) throws Throwable {
                binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        filmsAdapter.clearItems();
                        emitter.onNext(newText);
                        return false;
                    }
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        filmsAdapter.clearItems();
                        emitter.onNext(query);
                        return false;
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .map(t -> t.toLowerCase(Locale.getDefault()).trim())
                .debounce(1200, TimeUnit.MILLISECONDS)
                .filter(t -> {
                    if (t.isEmpty()) viewModel.getFilms(1);
                    return !t.isEmpty();
                })
                .flatMap(s -> viewModel.getFilmsBySearch(s, 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(err -> {
                    Log.e("InitSearchView", "onErrorReturn null" + err.getMessage());
                    viewModel.showProgressBar.onNext(0);
                    return null;
                })
                .subscribe(films -> {
                    filmsAdapter.addItems(films);
                        if (layoutManager.getItemCount() > 0) {
                            layoutManager.scrollToPosition(1);
                        }
                        pageNumber = 1;
                    },
                    throwable -> {
                        Toast.makeText(requireContext(),"Ошибка загрузки данных из сети.", Toast.LENGTH_SHORT).show();
                        Log.i("subscribe", throwable.getMessage());
                    });
        autoDisposable_j.add(data);


    }


    // Поиск по уже загруженным данным
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
        Log.i("layoutManager", String.valueOf(layoutManager.getItemCount()));
        //Применяем декоратор для отступов
        main_recycler.addItemDecoration(new TopSpacingItemDecoration_J(8));
        initRvScroller();
        //Кладем нашу БД в RV
        //filmsAdapter.addItems(filmsDataBase);
        /*
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
                    System.out.println("!!! " + " totalItemCount=" + totalItemCount + " lastVisiblesItems=" + lastVisibleItem + " pageNumber=" + pageNumber);
                    if (lastVisibleItem + FILMS_ITEM_SHIFT == FILMS_PER_PAGE * pageNumber - 1) {
                        String query = binding.searchView.getQuery().toString().trim();
                        //Делаем новый запрос фильмов на сервер
                        if (query.isEmpty()){
                            viewModel.getFilms(++pageNumber);
                        } else {
                            //filmsAdapter.addItems(viewModel.getFilmsBySearch(query, ++pageNumber).map());

                            viewModel.getFilmsBySearch(query, ++pageNumber).map(films -> films).
                                    observeOn(Schedulers.io()).
                                    subscribeOn(AndroidSchedulers.mainThread()).
                                    subscribe(films -> filmsAdapter.addItems(films), throwable -> {
                                Toast.makeText(requireContext(),"Ошибка загрузки данных из сети.", Toast.LENGTH_SHORT).show();
                                Log.i("subscribe", throwable.getMessage());
                            });

                            //Disposable subs = new Observer<>()

                            //viewModel.getFilmsBySearch(query, ++pageNumber);
                        }
                    }
                }
            }
        });
        */
    }

    private void initRvScroller() {
        Disposable dataRvScroller = Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Throwable {
                        binding.mainRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                    Log.i("initRvScroller", "!!!Rx " + " totalItemCount=" + totalItemCount + " lastVisiblesItems=" + lastVisibleItem + " pageNumber=" + pageNumber);
                                    if (lastVisibleItem + FILMS_ITEM_SHIFT == FILMS_PER_PAGE * pageNumber - 1) {
                                        emitter.onNext(++pageNumber);
                                    }
                                }
                            }
                        });
                    }
                }).observeOn(Schedulers.io())
                .retry(5)
//                .subscribe()
               .subscribe(page -> {
                   String query = binding.searchView.getQuery().toString().trim();
                   if (query.isEmpty() || query.trim() == "") {
                       viewModel.getFilms(page);
                   } else {
                       viewModel.getFilmsBySearch(query, page)
                           .subscribeOn(Schedulers.io())
                           .subscribe(
                               films -> filmsAdapter.addItems(films),
                               err -> {
                                   Toast.makeText(requireContext(), "Ошибка загрузки данных из сети.", Toast.LENGTH_SHORT).show();
                                   Log.e("subscribe 1", err.getMessage());
                               });
                           }
                        },
                   err -> {
                   Toast.makeText(requireContext(),"Ошибка загрузки данных из сети.", Toast.LENGTH_SHORT).show();
                   Log.e("subscribe 2", err.getMessage());
                });
        autoDisposable_j.add(dataRvScroller);
    }

    private void initRxErrorhandler(){
        RxJavaPlugins.setErrorHandler(e -> {
            Log.e("RxJava", e.getMessage());
            if (e instanceof UndeliverableException){
                e = e.getCause();
            }
            if (e instanceof IOException || e instanceof SocketException){
                return;
            }
            if (e instanceof InterruptedException){
                return;
            }
            if (e instanceof NullPointerException || e instanceof IllegalArgumentException){
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                return;
            }
            if (e instanceof IllegalStateException){

            }

        });

    }

    private void initRvScroller2() {
        Disposable dataRvScroller = Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Throwable {
                        binding.mainRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                    Log.i("initRvScroller", "!!!Rx " + " totalItemCount=" + totalItemCount + " lastVisiblesItems=" + lastVisibleItem + " pageNumber=" + pageNumber);
                                    if (lastVisibleItem + FILMS_ITEM_SHIFT == FILMS_PER_PAGE * pageNumber - 1) {
                                        String query = binding.searchView.getQuery().toString().trim();
                                        //emitter.onNext(++pageNumber);
                                        if (query.isEmpty() || query.trim() == ""){
                                            viewModel.getFilms(++pageNumber);
                                        } else {
                                            viewModel.getFilmsBySearch(query, ++pageNumber)
                                                    .subscribeOn(Schedulers.io())
                                                    .subscribe(films -> filmsAdapter.addItems(films),
                                                            err -> {
                                                                Toast.makeText(requireContext(),"Ошибка загрузки данных из сети.", Toast.LENGTH_SHORT).show();
                                                                Log.i("subscribe", err.getMessage());
                                                            });
                                        }
                                    }
                                }
                            }
                        });
                    }
                }).observeOn(Schedulers.io())
                .retry(5)
                .subscribe();
        autoDisposable_j.add(dataRvScroller);
    }


}