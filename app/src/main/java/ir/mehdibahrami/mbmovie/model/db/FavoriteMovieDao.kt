package ir.mehdibahrami.mbmovie.model.db

import androidx.room.*

@Dao
interface FavoriteMovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(favoriteMovieEntity: FavoriteMovieEntity)

    @Delete
    suspend fun deleteMovie(favoriteMovieEntity: FavoriteMovieEntity)

    @Query("SELECT * FROM favorites_table")
    suspend fun getAllFavoriteMovies(): List<FavoriteMovieEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites_table WHERE id= :movieId) ")
    suspend fun existsMovie(movieId: Int): Boolean
}