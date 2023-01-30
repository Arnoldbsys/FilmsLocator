package ru.dombuketa.filmslocaror.domain

import ru.dombuketa.filmslocaror.data.MainRepository

class Interactor(val repo: MainRepository) {
    fun getFilmsDB() : List<Film> = repo.filmsDataBase
}