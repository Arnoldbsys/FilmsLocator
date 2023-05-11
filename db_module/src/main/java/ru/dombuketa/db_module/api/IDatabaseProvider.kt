package ru.dombuketa.db_module.api

interface IDatabaseProvider {
    fun filmDAO(): IFilmDao
}