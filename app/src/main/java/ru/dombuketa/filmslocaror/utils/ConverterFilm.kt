package ru.dombuketa.filmslocaror.utils

import io.reactivex.rxjava3.core.Observable
import ru.dombuketa.filmslocaror.data.entity.TmdbFilm
import ru.dombuketa.filmslocaror.domain.Film

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