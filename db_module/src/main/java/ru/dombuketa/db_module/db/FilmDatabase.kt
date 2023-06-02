package ru.dombuketa.db_module.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.dombuketa.db_module.api.IDatabaseContract
import ru.dombuketa.db_module.dto.Film

@Database(entities = [Film::class], version = 1, exportSchema = false)
abstract class FilmDatabase : RoomDatabase(), IDatabaseContract {

}