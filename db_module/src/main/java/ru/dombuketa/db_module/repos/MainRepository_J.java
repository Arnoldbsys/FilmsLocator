package ru.dombuketa.db_module.repos;

import android.content.ContentValues;
import android.database.Cursor;
//import android.database.Observable;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import ru.dombuketa.db_module.db.DatabaseHelper_J;
import ru.dombuketa.db_module.api.IFilmDao_J;
import ru.dombuketa.db_module.dto.Film;
import ru.dombuketa.db_module.dto.Notification;

public class MainRepository_J {
    private DatabaseHelper_J databaseHelper_j;
    private IFilmDao_J filmDao_j;
    private SQLiteDatabase sqlDB;
    //Создаем курсор для обработки запросов из БД
    private Cursor cursor;

    @Inject
    public MainRepository_J(IFilmDao_J dao, DatabaseHelper_J dbh) {
        databaseHelper_j = dbh;
        filmDao_j = dao;
        //Инициализируем объект для взаимодействия с БД
        sqlDB = dbh.getReadableDatabase();
        //initDatabase();
    }

    public void putToDB(Film film){
        //Создаем объект, который будет хранить пары ключ-значение, для того
        //чтобы класть нужные данные в нужные столбцы
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper_J.COLUMN_TITLE, film.getTitle());
        cv.put(DatabaseHelper_J.COLUMN_POSTER, film.getPoster());
        cv.put(DatabaseHelper_J.COLUMN_DESCRIPTION, film.getDescription());
        cv.put(DatabaseHelper_J.COLUMN_RATING, film.getRating());
        //Кладем фильм в БД
        sqlDB.insert(DatabaseHelper_J.TABLE_NAME, null, cv);
    }

    public void putToDB(List<Film> films){
        //Запросы в БД должны быть в отдельном потоке - до RxJava
//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                filmDao_j.insertAll(films);
//            }
//        });
        if (films != null) {
            filmDao_j.insertAll(films);
        }
    }

    public Observable<List<Film>> getALLFromDB(){
        return filmDao_j.getCachedFilms();
    }

    //40*
    public void clearAllinDB() {
        Executors.newSingleThreadExecutor().execute(() -> filmDao_j.clearAllFilms());
    }
    //40*_

    public ArrayList<Film> getALLFromDBbyDBH(){
        //Создаем курсор на основании запроса "Получить все из таблицы"
        cursor = sqlDB.rawQuery("SELECT * FROM " + DatabaseHelper_J.TABLE_NAME, null );
        //Сюда будем сохранять результат получения данных
        ArrayList<Film> result = new ArrayList<>();
        //Проверяем, есть ли хоть одна строка в ответе на запрос
        if (cursor.moveToFirst()){
            //Итерируемся по таблице, пока есть записи, и создаем на основании объект Film
            do {
                String title = cursor.getString(1);
                String poster = cursor.getString(2);
                String description = cursor.getString(3);
                Double rating = cursor.getDouble(4);
                result.add(new Film(0, title, poster, description, rating, false));
            } while (cursor.moveToNext());
        }
        return result;
    }

    public List<Film> filmsDataBase = new ArrayList<Film>();
    // Нотификации
    public void deactivateAllNotification() {
        filmDao_j.clearAllNotifications();
    }

    public void deactivateNotification(int film_id) {
        filmDao_j.deactivateNotification(film_id);
    }

    public void putNotificationToDB(Notification notif) {
        // Для упрощения деактивирую старый и вставляю новый
        filmDao_j.deactivateNotification(notif.getFilmId());
        filmDao_j.insertNotification(notif);
    }

    public Observable<List<Notification>> getActiveNotifications() {
        return filmDao_j.getNotifications();
    }
    //_ Нотификации
}
