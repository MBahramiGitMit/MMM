package ir.mehdibahrami.mbmovie.model.repository

import ir.mehdibahrami.mbmovie.model.db.FavoriteMovieDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(private val favoriteMovieDao: FavoriteMovieDao) {
    suspend fun getAllFavoriteMovies() = favoriteMovieDao.getAllFavoriteMovies()
}