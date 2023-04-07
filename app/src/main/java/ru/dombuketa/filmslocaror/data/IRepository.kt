package ru.dombuketa.filmslocaror.data

import ru.dombuketa.filmslocaror.domain.Film

interface IRepository {
    val filmsDataBase : ArrayList<Film>
}