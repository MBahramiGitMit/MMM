package ir.mehdibahrami.mbmovie.model.net

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("v1/movies")
    suspend fun getAllMovies(@Query("page") page: Int): Response<MoviesListResponse>

    @GET("v1/genres")
    suspend fun getAllGenres(): Response<List<GenresListItemResponse>>

    @GET("v1/movies/{movie_id}")
    suspend fun getMovieDetail(@Path("movie_id") id: Int): Response<MovieDetailsResponse>

    @GET("v1/movies")
    suspend fun getMoviesByName(
        @Query("q") q: String,
        @Query("page") page: Int
    ): Response<MoviesListResponse>

    @GET("v1/genres/{genre_id}/movies")
    suspend fun getMoviesByGenre(
        @Path("genre_id") genreId: Int,
        @Query("page") page: Int
    ): Response<MoviesListResponse>



}