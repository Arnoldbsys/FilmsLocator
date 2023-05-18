package ru.dombuketa.filmslocaror.utils

import ru.dombuketa.net_module.entity.TmdbFilm

object ConverterFilm {
    fun convertApiListToDTOList(list: List<TmdbFilm>?): List<Film>{
        val result = mutableListOf<Film>()
        list?.forEach {
            result.add(Film(
                id = it.id,
                title =  it.title,
                poster = it.posterPath,
                description = it.overview,
                rating = it.voteAverage,
                isInFavorites = false
            )
            )
        }
        return  result
    }
}

