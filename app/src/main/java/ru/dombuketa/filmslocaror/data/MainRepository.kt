package ru.dombuketa.filmslocaror.data

import ru.dombuketa.filmslocaror.R
import ru.dombuketa.filmslocaror.domain.Film
import javax.inject.Inject

class MainRepository @Inject constructor() : IRepository {
    override val filmsDataBase = ArrayList<Film>()
}