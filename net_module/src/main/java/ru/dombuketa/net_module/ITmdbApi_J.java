package ru.dombuketa.net_module;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.dombuketa.net_module.entity.TmdbFilm;
import ru.dombuketa.net_module.entity.TmdbResultsDTO;
//import ru.dombuketa.filmslocaror.data.entity.TmdbResultsDTO;

public interface ITmdbApi_J {
    @GET("3/movie/{id}")
    Observable<TmdbFilm> getFilm(
            @Path("id") int category,
            @Query("api_key") String apiKey,
            @Query("language") String language);
    @GET("3/movie/{category}")
    Call<TmdbResultsDTO> getFilms(
            @Path("category") String category,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page);
    @GET("3/movie/{category}")
    Observable<TmdbResultsDTO> getFilmsRx(
            @Path("category") String category,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page);
    // Поиск
    @GET("3/search/movie")
    Observable<TmdbResultsDTO> getFilmsBySearch(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query,
            @Query("page") int page);
}
