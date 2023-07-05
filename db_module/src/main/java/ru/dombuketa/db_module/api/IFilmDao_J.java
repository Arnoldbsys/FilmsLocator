package ru.dombuketa.db_module.api;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;
import io.reactivex.rxjava3.core.Observable;
import ru.dombuketa.db_module.dto.Film;
import ru.dombuketa.db_module.dto.Notification;

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

    // Notifications (Уведомления)
    @Query("SELECT * FROM notifications WHERE is_active = 1 ")
    Observable<List<Notification>> getNotifications();

    //Кладём списком в БД, в случае конфликта перезаписываем
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNotification(Notification notification);

    @Query("UPDATE notifications SET is_active = 0 WHERE film_id = :film_id")
    void deactivateNotification(int film_id);

    @Query("UPDATE notifications SET is_active = 0")
    void clearAllNotifications();
}
