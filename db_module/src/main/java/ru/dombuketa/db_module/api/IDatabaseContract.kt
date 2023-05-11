package ru.dombuketa.db_module.api

interface IDatabaseContract {
    fun filmDao(): IFilmDao
}