package ru.dombuketa.db_module.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Observable
import ru.dombuketa.db_module.dto.Film
import ru.dombuketa.db_module.dto.Notification

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

    // Notifications (Уведомления)
    @Query("SELECT * FROM notifications WHERE is_active = 1 ")
    fun getNotifications() : Observable<List<Notification>>

    //Кладём списком в БД, в случае конфликта перезаписываем
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotification(notification: Notification)

    @Query("UPDATE notifications SET is_active = 0 WHERE film_id = :film_id")
    fun deactivateNotification(film_id : Int)

    @Query("UPDATE notifications SET is_active = 0")
    fun clearAllNotifications()

}