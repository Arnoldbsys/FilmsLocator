package ru.dombuketa.filmslocaror.data.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.dombuketa.filmslocaror.data.dao.IFilmDao;
import ru.dombuketa.filmslocaror.data.dao.IFilmDao_J;
import ru.dombuketa.filmslocaror.domain.Film;

@Database(entities = {Film.class}, version = 1, exportSchema = false)
abstract public class AppDatabase_J extends RoomDatabase {
    public abstract IFilmDao_J filmDao_J();
}
