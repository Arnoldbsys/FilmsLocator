package ru.dombuketa.db_module.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {
    val MIGRATION_1_2 = object: Migration(1, 2){
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE Notifications(id INTEGER PRIMARY KEY NOT NULL," +
                    "    film_id INTEGER NOT NULL, " +
                    "    start_time TEXT NOT NULL, is_active INTEGER DEFAULT 1 NOT NULL ,\n" +
                    "    start_year INTEGER NOT NULL,\n" +
                    "    start_month INTEGER NOT NULL,\n" +
                    "    start_day INTEGER NOT NULL,\n" +
                    "    start_hour INTEGER NOT NULL,\n" +
                    "    start_minute INTEGER NOT NULL)")
        }
    }
    val MIGRATION_2_3 = object: Migration(2, 3){
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE Notifications ADD COLUMN poster_path TEXT")
        }
    }
    val MIGRATION_3_4 = object: Migration(3, 4){
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE Notifications ADD COLUMN title TEXT")
        }
    }
    val MIGRATION_4_5 = object: Migration(4, 5){
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE Notifications DROP COLUMN start_time")
        }
    }
}