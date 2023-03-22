package ru.dombuketa.filmslocaror.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import javax.inject.Inject;

import ru.dombuketa.filmslocaror.domain.Film;

//Помечаем, что это не просто интерфейс, а Dao-объект
@Dao
public interface IFilmDao_J {
    //Запрос на всю таблицу
    @Query("SELECT * FROM cached_films")
    public List<Film> getCachedFilms();
    //Кладём списком в БД, в случае конфликта перезаписываем
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<Film> list);
}
