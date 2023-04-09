package ru.dombuketa.filmslocaror.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Observable
import ru.dombuketa.filmslocaror.domain.Film

//Помечаем, что это не просто интерфейс, а Dao-объект
@Dao
interface IFilmDao {
    //Запрос на всю таблицу
    @Query("SELECT * FROM cached_films")
    fun getCachedFilms() : Observable<List<Film>>
    //Кладём списком в БД, в случае конфликта перезаписываем
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Film>)

    //40* Очистка таблицы
    @Query("DELETE FROM cached_films")
    fun clearAllFilms()
    //40*_

}