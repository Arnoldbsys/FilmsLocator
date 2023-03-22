package ru.dombuketa.filmslocaror.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import ru.dombuketa.filmslocaror.App_J;
import ru.dombuketa.filmslocaror.domain.Film;
import ru.dombuketa.filmslocaror.domain.Interactor_J;
import ru.dombuketa.filmslocaror.viewmodel.HomeFragmentViewModel_J;

public class PreferenceProvider_J {
    //Ключи для наших настроек, по ним мы их будем получать
    private final String KEY_FIRST_LAUNCH = "first_launch";
    public static final String KEY_DEFAULT_CATEGORY = "default_category";
    private final String DEFAULT_CATEGORY = "popular";
    private final String LAST_TIME_INTERNET_OK = "last_time_internet_ok"; //40*
    //Нам нужен контекст приложения
    private Context appContext;
    //Создаем экземпляр SharedPreferences
    private SharedPreferences preferences;
    //38*
    public MutableLiveData<String> currentCategory = new MutableLiveData<String>();
    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener;
    //38*_

    public PreferenceProvider_J(Context context) {
        this.appContext = context.getApplicationContext();
        preferences = appContext.getSharedPreferences("settings_j", Context.MODE_PRIVATE);
        //Логика для первого запуска приложения, чтобы положить наши настройки,
        //Сюда потом можно добавить и другие настройки
        if (preferences.getBoolean(KEY_FIRST_LAUNCH, false)){
            preferences.edit().putString(KEY_DEFAULT_CATEGORY, DEFAULT_CATEGORY).apply();
            preferences.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply();
        }
        //38*
        currentCategory.setValue(getDefaultCategory());
        setSharedListener();
        //38*_
    }
    //Сохраняем категорию
    public void saveDefaultCategory(String category){
        preferences.edit().putString(KEY_DEFAULT_CATEGORY, category).apply();
    }
    //Забираем категорию
    public String getDefaultCategory(){
        return preferences.getString(KEY_DEFAULT_CATEGORY, DEFAULT_CATEGORY);
    }
    //38*
    private void setSharedListener() {
        SharedPreferences.OnSharedPreferenceChangeListener listener = (sharedPreferences, s) -> {
            switch (s) {
                case (PreferenceProvider_J.KEY_DEFAULT_CATEGORY):
                    currentCategory.setValue(getDefaultCategory());
                    break;
            }
        };
        sharedPreferenceChangeListener = listener;
        preferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }
    //38*_
    //40*
    public long getLastTimeInternetOK(){
        return preferences.getLong(LAST_TIME_INTERNET_OK, 1672520400000L); // 01.01.2023
    }
    public void setLastTimeInternetOK(long time){
        preferences.edit().putLong(LAST_TIME_INTERNET_OK, time).apply();
    }
    //40*_
}
