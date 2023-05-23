package ru.dombuketa.db_module.api;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;
import io.reactivex.rxjava3.core.Observable;
import ru.dombuketa.db_module.dto.Film;

//Помечаем, что это не просто интерфейс, а Dao-объект
@Dao
public interface IFilmDao_J {
    //Запрос на всю таблицу
    @Query("SELECT * FROM cached_films")
    //public LiveData<List<Film>> getCachedFilms();
    public Observable<List<Film>> getCachedFilms();
    //Кладём списком в БД, в случае конфликта перезаписываем
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<Film> list);
    //40* Очистка таблицы
    @Query("DELETE FROM cached_films")
    public void clearAllFilms();
    //40*_
}