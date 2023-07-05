package ru.dombuketa.filmslocaror.utils

import ru.dombuketa.db_module.dto.Film
import ru.dombuketa.net_module.entity.TmdbFilm

object ConverterFilm {

    fun convertApiListToDTOList(list: List<TmdbFilm>?): List<Film>{
        val result = mutableListOf<Film>()
        list?.forEach {
            result.add(convertApiFilmToDTOFilm(it))
        }
        return  result
    }

    fun convertApiFilmToDTOFilm(film: TmdbFilm): Film {
        return Film(
            id = film.id,
            title =  film.title,
            poster = film.posterPath,
            description = film.overview,
            rating = film.voteAverage,
            isInFavorites = false
        )
    }

}

