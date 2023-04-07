package ru.dombuketa.filmslocaror.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import androidx.lifecycle.LiveData;

import java.sql.SQLData;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import ru.dombuketa.filmslocaror.R;
import ru.dombuketa.filmslocaror.data.dao.IFilmDao_J;
import ru.dombuketa.filmslocaror.data.db.DatabaseHelper_J;
import ru.dombuketa.filmslocaror.domain.Film;

public class MainRepository_J {
    private DatabaseHelper_J databaseHelper_j;
    private IFilmDao_J filmDao_j;

    private SQLiteDatabase sqlDB;
    //Создаем курсор для обработки запросов из БД
    private Cursor cursor;


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
        //Запросы в БД должны быть в отдельном потоке
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                filmDao_j.insertAll(films);
            }
        });
    }

    public LiveData<List<Film>> getALLFromDB(){
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

    private void initDatabase(){
        filmsDataBase = new ArrayList<Film>();
        filmsDataBase.add(new Film(1,"Титаник", String.valueOf(R.drawable.poster_1),"Очень хороший фильм про любовь, но я его не смотрел.", 1f,false));
        filmsDataBase.add(new Film(2,"Тар", String.valueOf(R.drawable.poster_2),"Не знаю, про что фильм, но по картинке похоже, что глубокомысленные, тоже не смотрел.", 7.7f,false));
        filmsDataBase.add(new Film(3,"ГудВилХантер",String.valueOf(R.drawable.poster_3),"Кто-то кого-то искал и нашел, а может и не нашел, можно посмотреть если делать нечего.", 0f,true));
        filmsDataBase.add(new Film(4,"Шининг", String.valueOf(R.drawable.poster_4),"Фильм про мальчика на велосипеде и двух сестер в коридоре. Затронута тема безопасности езды в помещениях. Неплохой фильм для расширения кругозора.", 5.5f,false));
        filmsDataBase.add(new Film(5,"Завтрак клуб",String.valueOf(R.drawable.poster_5),"Фильм про то, что нужно брать с собой на природу чтобы не выглядеть глупо перед сверстниками во время приема пищи. Старый добрый фильм про взаимовыручку и жадность.", 9f,false));
        filmsDataBase.add(new Film(6,"Криминальное чтиво",String.valueOf(R.drawable.poster_01),"Все уже хорошо знают эти четыре знаменитых сюжета, где тесно сплетаются судьбы незнакомых людей.", 0f,false));
        filmsDataBase.add(new Film(7,"Звездные войны",String.valueOf(R.drawable.poster_02),"2014 год. Дарт Вэйдер хочет встать во главе Космической Федерации, но на пути у него встает с бластером ловкий Люк с Чабукой. Захватывающий фильм про будущее.", 0f, false));
        filmsDataBase.add(new Film(8,"Е.Т.", String.valueOf(R.drawable.poster_03),"Приключения велосипедиста на луне. Молодой человек после вечеринки обнаруживает себя на велосипеде на луне. Фильм, пропагандирующий трезвый образ жизни. Обязателен к просмотру молодежи. Сам не смотрел.", 0f, true));
        filmsDataBase.add(new Film(9,"Назад в будующее", String.valueOf(R.drawable.poster_04),"Гениальный и неженатый профессор изобрел машину времени, которая помогла ему в итоге найти спутницу жизни при помощи паровоза.", 1f,false));
    }


}
