package ru.dombuketa.filmslocaror.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.dombuketa.filmslocaror.data.entity.TmdbResultsDTO

interface ITmdbApi {
    @GET("3/movie/{category}")
    fun getFilms(
        @Path("category") category: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ) : Call<TmdbResultsDTO>
}