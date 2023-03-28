package ru.dombuketa.filmslocaror.utils;

import java.util.ArrayList;
import java.util.List;

import ru.dombuketa.filmslocaror.data.entity.TmdbFilm;
import ru.dombuketa.filmslocaror.domain.Film;

public class ConverterFilm_J {
    public static List<Film> convertApiListToDTOList(List<TmdbFilm> list){
        List<Film> result = new ArrayList<Film>();
        if (list != null){
            for (TmdbFilm it : list) {
                result.add(new Film(
                        it.getId(),
                        it.getTitle(),
                        it.getPosterPath(),
                        it.getOverview(),
                        it.getVoteAverage(),
                        false));
            }
        }
        return result;
    }
}