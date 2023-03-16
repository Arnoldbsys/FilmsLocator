package ru.dombuketa.filmslocaror.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.dombuketa.filmslocaror.data.entity.TmdbResultsDTO;

public interface ITmdbApi_J {
    @GET("3/movie/{category}")
    Call<TmdbResultsDTO> getFilms(
            @Path("category") String category,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page);
}
