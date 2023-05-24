package ru.dombuketa.filmslocaror.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.dombuketa.filmslocaror.R;
import ru.dombuketa.filmslocaror.databinding.FragmentSettingsBinding;
import ru.dombuketa.filmslocaror.utils.AnimationHelper_J;
import ru.dombuketa.filmslocaror.viewmodel.HomeFragmentViewModel_J;
import ru.dombuketa.filmslocaror.viewmodel.SettingsFragmentViewModel_J;

public class SettingsFragment_J extends Fragment {
    private final String POPULAR_CATEGORY = "popular";
    private final String TOP_RATED_CATEGORY = "top_rated";
    private final String UPCOMING_CATEGORY = "upcoming";
    private final String NOW_PLAYING_CATEGORY = "now_playing";

    private FragmentSettingsBinding binding;
    private SettingsFragmentViewModel_J viewModel;
    private SettingsFragmentViewModel_J getViewModel() {
        if (viewModel == null){
            viewModel = new ViewModelProvider(this).get(SettingsFragmentViewModel_J.class);
        }
        return viewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentSettingsBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Подключаем анимации и передаем номер позиции у кнопки в нижнем меню
        AnimationHelper_J.performFragmentCircularRevealAnimation(binding.settingsFragmentRoot,requireActivity(), 5);
        //Слушаем, какой у нас сейчас выбран вариант в настройках
        getViewModel().categoryPropertyLiveData.observe(getViewLifecycleOwner(),s -> {
            switch (s){
                case POPULAR_CATEGORY:
                    binding.radioGroup.check(R.id.radio_popular);
                    break;
                case TOP_RATED_CATEGORY:
                    binding.radioGroup.check(R.id.radio_top_rated);
                    break;
                case UPCOMING_CATEGORY:
                    binding.radioGroup.check(R.id.radio_upcoming);
                    break;
                case NOW_PLAYING_CATEGORY:
                    binding.radioGroup.check(R.id.radio_now_playing);
                    break;
            }
        });
        //Слушатель для отправки нового состояния в настройки
        binding.radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i){
                case R.id.radio_popular:
                    viewModel.putCategoryProperty(POPULAR_CATEGORY);
                    break;
                case R.id.radio_top_rated:
                    viewModel.putCategoryProperty(TOP_RATED_CATEGORY);
                    break;
                case R.id.radio_upcoming:
                    viewModel.putCategoryProperty(UPCOMING_CATEGORY);
                    break;
                case R.id.radio_now_playing:
                    viewModel.putCategoryProperty(NOW_PLAYING_CATEGORY);
                    break;
            }
        });
        binding.radioGroupTheme.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i){
                case R.id.radio_light:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
                case R.id.radio_dark:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
            }
        });
    }
}
