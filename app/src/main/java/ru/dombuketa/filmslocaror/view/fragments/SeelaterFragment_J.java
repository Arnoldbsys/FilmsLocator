package ru.dombuketa.filmslocaror.view.fragments;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ru.dombuketa.db_module.dto.Notification;
import ru.dombuketa.filmslocaror.App_J;
import ru.dombuketa.filmslocaror.R;
import ru.dombuketa.filmslocaror.databinding.FragmentSeelaterBinding;
import ru.dombuketa.filmslocaror.utils.AnimationHelper_J;
import ru.dombuketa.filmslocaror.utils.AutoDisposable_J;
import ru.dombuketa.filmslocaror.utils.TopSpacingItemDecoration_J;
import ru.dombuketa.filmslocaror.view.rv_adapters.NotificationListRecyclerAdapter_J;
import ru.dombuketa.filmslocaror.view.rv_helpers.IItemTouchHelperAdapter_J;
import ru.dombuketa.filmslocaror.view.rv_helpers.SeelaterTouchHelper;
import ru.dombuketa.filmslocaror.view.rv_helpers.SeelaterTouchHelper_J;
import ru.dombuketa.filmslocaror.viewmodel.SeelaterFragmentViewModel_J;

public class SeelaterFragment_J extends Fragment {
    private FragmentSeelaterBinding binding;
    private SeelaterFragmentViewModel_J viewModel_j;
    private NotificationListRecyclerAdapter_J notificationAdapter_j;
    private LinearLayoutManager layoutManagerRV;
    private AutoDisposable_J autoDispose = new AutoDisposable_J();

    private SeelaterFragmentViewModel_J getViewModel_j(){
        return viewModel_j == null ?
                //viewModel_j = new ViewModelProvider.NewInstanceFactory().create(SeelaterFragmentViewModel_J.class) :
                viewModel_j = new ViewModelProvider(this).get(SeelaterFragmentViewModel_J.class) :
                viewModel_j;
    }

    public SeelaterFragment_J() {
        //App_J.getInstance().daggerj.injectt(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSeelaterBinding.inflate(inflater, container, false);
        autoDispose.bindTo(getLifecycle());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ConstraintLayout seelater_fragment_root = requireActivity().findViewById(R.id.seelater_fragment_root);
        AnimationHelper_J.performFragmentCircularRevealAnimation(seelater_fragment_root, requireActivity(),3);

        initSeeLaterRVJ();
        autoDispose.add(
        getViewModel_j().notificationsListRxJavaData
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                list -> notificationAdapter_j.addItems(list),
                err -> System.out.println("!!! Seelater - initRV/ Create" + err.getMessage())
            )
        );
        binding.btnReminders.setOnClickListener(l -> {
            viewModel_j.clearAllNotifications();
            Log.i("fragSeeLater_J", "click clear all");
        });
    }

    void initSeeLaterRVJ() {
        //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
        //оставим его пока пустым, он нам понадобится во второй части задания
        notificationAdapter_j = new NotificationListRecyclerAdapter_J(notif -> {
            Log.i("seeLaterFragment_J", "!!! Click");
            viewModel_j.getFilm(notif.getFilmId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    film -> App_J.getInstance().daggerj.getNotifyHelper_j().notificationSet_J(requireContext(), film),
                    err ->  System.out.println("!!! Seelater - initRV/Click " + err.getMessage())
                );
        });
        RecyclerView rv = requireActivity().findViewById(R.id.seelater_recycler);
        //Присваиваем адаптер
        rv.setAdapter(notificationAdapter_j);
        //Удаление через смахивание
        SeelaterTouchHelper_J callback = new SeelaterTouchHelper_J(notificationAdapter_j);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv);
        //Присвои layoutmanager
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        layoutManagerRV = (LinearLayoutManager) rv.getLayoutManager();
        //Применяем декоратор для отступов
        TopSpacingItemDecoration_J decoration_j = new TopSpacingItemDecoration_J(6);
        rv.addItemDecoration(decoration_j);
    }
}
