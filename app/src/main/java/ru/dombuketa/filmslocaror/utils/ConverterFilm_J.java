package ru.dombuketa.filmslocaror.utils;

import java.util.ArrayList;
import java.util.List;

//import ru.dombuketa.filmslocaror.data.entity.TmdbFilm;
import ru.dombuketa.db_module.dto.Film;
import ru.dombuketa.net_module.entity.TmdbFilm;

public class ConverterFilm_J {
//    public static List<Film> convertApiListToDTOList(List<TmdbFilm> list){
//        List<Film> result = new ArrayList<Film>();
//        if (list != null){
//            for (TmdbFilm it : list) {
//                result.add(new Film(
//                        it.getId(),
//                        it.getTitle(),
//                        it.getPosterPath(),
//                        it.getOverview(),
//                        it.getVoteAverage(),
//                        false));
//            }
//        }
//        return result;
//    }

    public static List<Film> convertApiListToDTOList(List<TmdbFilm> list){
        List<Film> result = new ArrayList<Film>();
        if (list != null){
            for (TmdbFilm it : list) {
                result.add(convertApiFilmToDTOFilm(it));
            }
        }
        return result;
    }

    public static Film convertApiFilmToDTOFilm(TmdbFilm film){
        return new Film(
            film.getId(),
            film.getTitle(),
            film.getPosterPath(),
            film.getOverview(),
            film.getVoteAverage(),
            false);
    }

}
