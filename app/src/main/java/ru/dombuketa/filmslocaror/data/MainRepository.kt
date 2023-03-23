package ru.dombuketa.filmslocaror.data

import android.content.ContentValues
import android.database.Cursor
import androidx.lifecycle.LiveData
import ru.dombuketa.filmslocaror.data.dao.IFilmDao
import ru.dombuketa.filmslocaror.data.db.DatabaseHelper
import ru.dombuketa.filmslocaror.domain.Film
import java.util.concurrent.Executors

//class MainRepository(databaseHelper: DatabaseHelper)  {
class MainRepository(private val filmDao: IFilmDao, databaseHelper: DatabaseHelper)  {
    //Инициализируем объект для взаимодействия с БД
    private val sqlDB = databaseHelper.readableDatabase
    //Создаем курсор для обработки запросов из БД
    private lateinit var cursor: Cursor

    fun putToDB(films: List<Film>){
        //Запросы в БД должны быть в отдельном потоке
        Executors.newSingleThreadExecutor().execute { filmDao.insertAll(films) }
    }

    fun putToDB(film: Film){
        //Создаем объект, который будет хранить пары ключ-значение, для того
        //чтобы класть нужные данные в нужные столбцы
        val cv = ContentValues()
        cv.apply {
            put(DatabaseHelper.COLUMN_TITLE, film.title)
            put(DatabaseHelper.COLUMN_POSTER, film.poster)
            put(DatabaseHelper.COLUMN_DESCRIPTION, film.description)
            put(DatabaseHelper.COLUMN_RATING, film.rating)
        }
        //Кладем фильм в БД
        sqlDB.insert(DatabaseHelper.TABLE_NAME, null, cv)
    }

    fun getAllFromDB() : LiveData<List<Film>> {
        return filmDao.getCachedFilms()
    }

    //40*
    fun clearAllinDB() {
        Executors.newSingleThreadExecutor().execute { filmDao.clearAllFilms() }
    }

    //40*_
    fun getAllFromDBbyDBH(): List<Film>{
        //Создаем курсор на основании запроса "Получить все из таблицы"
        cursor = sqlDB.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_NAME}", null)
        //Сюда будем сохранять результат получения данных
        val result = mutableListOf<Film>()
        //Проверяем, есть ли хоть одна строка в ответе на запрос
        if (cursor.moveToFirst()){
            //Итерируемся по таблице, пока есть записи, и создаем на основании объект Film
            do {
                val title = cursor.getString(1)
                val poster = cursor.getString(2)
                val description = cursor.getString(3)
                val rating = cursor.getDouble(4)
                result.add(Film(0,title, poster, description, rating, false))
            } while (cursor.moveToNext())
        }
        return result
    }
    // Очистка БД
    fun clearDB(){
        cursor = sqlDB.rawQuery("DELETE FROM ${DatabaseHelper.TABLE_NAME}", null)
    }
    // Выборка всех фильмов в диапазоне рейтингов
    fun getAllFromDBByRating(ratingFrom: Double, ratingTo: Double): List<Film>{
        cursor = sqlDB.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_NAME} WHERE ${DatabaseHelper.COLUMN_RATING} BETWEEN ${ratingFrom} AND ${ratingTo}", null)
        val result = mutableListOf<Film>()
        if (cursor.moveToFirst()){
            do {
                val title = cursor.getString(1)
                val poster = cursor.getString(2)
                val description = cursor.getString(3)
                val rating = cursor.getDouble(4)
                result.add(Film(0,title, poster, description, rating, false))
            } while (cursor.moveToNext())
        }
        return result
    }



    val filmsDataBase = ArrayList<Film>()
}