package ru.dombuketa.db_module.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import javax.inject.Inject

class DatabaseHelper @Inject constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_TITLE TEXT," +
                    //"$COLUMN_TITLE TEXT UNIQUE," +
                    "$COLUMN_POSTER TEXT," +
                    "$COLUMN_DESCRIPTION TEXT," +
                    "$COLUMN_RATING REAL);"
        )
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    companion object{
        private const val DATABASE_NAME = "films.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "films_table"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_POSTER = "poster_path"
        const val COLUMN_DESCRIPTION = "overview"
        const val COLUMN_RATING = "vote_average"
    }
}