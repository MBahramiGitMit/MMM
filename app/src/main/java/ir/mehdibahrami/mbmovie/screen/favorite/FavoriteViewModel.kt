package ir.mehdibahrami.mbmovie.screen.favorite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mehdibahrami.mbmovie.model.db.FavoriteMovieEntity
import ir.mehdibahrami.mbmovie.model.repository.FavoriteRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: FavoriteRepository) :
    ViewModel() {

    var favoriteMoviesListState by mutableStateOf(emptyList<FavoriteMovieEntity>())

    fun loadFavoriteMovies() {
        viewModelScope.launch {
            favoriteMoviesListState = repository.getAllFavoriteMovies()
        }
    }

}