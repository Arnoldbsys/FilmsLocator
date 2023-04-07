package ru.dombuketa.filmslocaror.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper_J extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "films_j.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "films_table";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_POSTER = "poster_path";
    public static final String COLUMN_DESCRIPTION = "overview";
    public static final String COLUMN_RATING = "vote_average";

    private Context context;

    public DatabaseHelper_J(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (db != null){
            db.execSQL(
                    "CREATE TABLE " +TABLE_NAME +" (" +
                            COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                            COLUMN_TITLE +" TEXT UNIQUE," +
                            COLUMN_POSTER + " TEXT," +
                            COLUMN_DESCRIPTION + " TEXT," +
                            COLUMN_RATING + " REAL);"
            );
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { }
}
