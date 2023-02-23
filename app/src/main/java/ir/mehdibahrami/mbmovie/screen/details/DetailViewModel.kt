package ir.mehdibahrami.mbmovie.screen.details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mehdibahrami.mbmovie.model.db.FavoriteMovieEntity
import ir.mehdibahrami.mbmovie.model.net.MovieDetailsResponse
import ir.mehdibahrami.mbmovie.model.repository.DetailsRepository
import ir.mehdibahrami.mbmovie.util.NetworkChecker
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val networkChecker: NetworkChecker,
    private val repository: DetailsRepository
) : ViewModel() {
    var movieDetailState by mutableStateOf(MovieDetailsResponse())
    var isFavoriteMovieState by mutableStateOf(false)
    var isLoadingState by mutableStateOf(true)

    var isOnline by mutableStateOf(networkChecker.isNetConnected())

    fun initLoad(movieId: Int) {
        if (networkChecker.isNetConnected()) {
            isOnline = true
            loadMovieDetails(movieId)
            checkIsFavoriteMovie(movieId)
        } else {
            isOnline = false
        }
    }

    fun onFavoriteIconClicked() {
        if (isFavoriteMovieState) {
            deleteFromFavorite()
        } else {
            addToFavorite()
        }
    }

    private fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            val response = repository.getMovieDetail(movieId = movieId)
            if (response.isSuccessful) {
                movieDetailState = response.body()!!
            } else {
                Log.e("7575", "getMovieDetails: DetailViewModel")
            }
            isLoadingState = false
        }
    }

    private fun addToFavorite() {
        viewModelScope.launch {
            val favoriteMovieEntity = FavoriteMovieEntity(
                id = movieDetailState.id!!,
                title = movieDetailState.title!!,
                poster = movieDetailState.poster!!,
                imdbRating = movieDetailState.imdbRating!!
            )
            repository.insertFavoriteMovie(favoriteMovieEntity = favoriteMovieEntity)
            checkIsFavoriteMovie()
        }
        checkIsFavoriteMovie()
    }

    private fun deleteFromFavorite() {
        viewModelScope.launch {
            val favoriteMovieEntity = FavoriteMovieEntity(
                id = movieDetailState.id!!,
                title = movieDetailState.title!!,
                poster = movieDetailState.poster!!,
                imdbRating = movieDetailState.imdbRating!!
            )
            repository.deleteFavoriteMovie(favoriteMovieEntity = favoriteMovieEntity)
            checkIsFavoriteMovie()
        }
    }

    private fun checkIsFavoriteMovie(movieId: Int = movieDetailState.id!!) {
        viewModelScope.launch {
            isFavoriteMovieState = repository.isMovieFavorite(movieId)
        }
    }
}