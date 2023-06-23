package ru.dombuketa.db_module.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.dombuketa.db_module.api.IDatabaseContract_J;
import ru.dombuketa.db_module.dto.Film;
import ru.dombuketa.db_module.dto.Notification;

@Database(entities = {Film.class, Notification.class}, version = 5, exportSchema = false)
abstract public class FilmDatabase_J extends RoomDatabase implements IDatabaseContract_J {

}
