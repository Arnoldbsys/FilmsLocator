package ru.dombuketa.filmslocaror.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.dombuketa.filmslocaror.data.dao.IFilmDao
import ru.dombuketa.filmslocaror.domain.Film

@Database(entities = [Film::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao() : IFilmDao
}