package ru.dombuketa.filmslocaror.data

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.lifecycle.MutableLiveData

class PreferenceProvider(context: Context) {
    //Нам нужен контекст приложения
    private val appContext = context.applicationContext
    //Создаем экземпляр SharedPreferences
    private val preference: SharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE)
    //38*.
    var currentCategory = MutableLiveData<String>()
    private lateinit var sharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener
    //38*_
    init{
        //Логика для первого запуска приложения, чтобы положить наши настройки,
        //Сюда потом можно добавить и другие настройки
        if (preference.getBoolean(KEY_FIRST_LAUNCH, false)){
            preference.edit().putString(KEY_DEFAULT_CATEGORY, DEFAULT_CATEGORY).apply()
            preference.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
        }
        //38*
        currentCategory.value = getDefaultCategory()
        setSharedListener()
        //38*_
    }

    //Сохраняем категорию
    fun saveDefaultCategory(category: String){
        preference.edit().putString(KEY_DEFAULT_CATEGORY, category).apply()
    }
    //Забираем категорию
    fun getDefaultCategory() : String{
        return preference.getString(KEY_DEFAULT_CATEGORY, DEFAULT_CATEGORY) ?: DEFAULT_CATEGORY
    }

    //38*
    private fun setSharedListener(){
        val listener = OnSharedPreferenceChangeListener { sharedPreferences: SharedPreferences?, s: String? ->
                when (s) {
                    PreferenceProvider_J.KEY_DEFAULT_CATEGORY -> {
                        currentCategory.setValue(getDefaultCategory())
                    }
                }
            }
        // Из-за сборщика мусора приходится такую штуку мастерить, иначе он вычищает листенер
        sharedPreferenceChangeListener = listener
        preference.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
    }
    //38*_
    //40*
    open fun getLastTimeInternetOK(): Long {
        return preference.getLong(LAST_TIME_INTERNET_OK, 1672520400000L) // 01.01.2023
    }

    fun setLastTimeInternetOK(time: Long) {
        preference.edit().putLong(LAST_TIME_INTERNET_OK, time).apply()
    }
    //40*_



    //Ключи для наших настроек, по ним мы их будем получать
    companion object{
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_DEFAULT_CATEGORY = "default_category"
        private const val DEFAULT_CATEGORY = "popular"
        private const val LAST_TIME_INTERNET_OK = "last_time_internet_ok" //40*

    }
}