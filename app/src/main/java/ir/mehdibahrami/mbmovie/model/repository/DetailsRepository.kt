package ir.mehdibahrami.mbmovie.model.repository

import ir.mehdibahrami.mbmovie.model.db.FavoriteMovieDao
import ir.mehdibahrami.mbmovie.model.db.FavoriteMovieEntity
import ir.mehdibahrami.mbmovie.model.net.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailsRepository @Inject constructor(
    private val apiService: ApiService,
    private val favoriteMovieDao: FavoriteMovieDao
) {
    suspend fun getMovieDetail(movieId: Int) = apiService.getMovieDetail(id = movieId)

    suspend fun insertFavoriteMovie(favoriteMovieEntity: FavoriteMovieEntity) =
        favoriteMovieDao.insertMovie(favoriteMovieEntity = favoriteMovieEntity)

    suspend fun deleteFavoriteMovie(favoriteMovieEntity: FavoriteMovieEntity) =
        favoriteMovieDao.deleteMovie(favoriteMovieEntity = favoriteMovieEntity)

    suspend fun isMovieFavorite(movieId: Int) = favoriteMovieDao.existsMovie(movieId = movieId)
}