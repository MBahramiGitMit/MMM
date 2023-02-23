package ir.mehdibahrami.mbmovie.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mehdibahrami.mbmovie.model.net.GenresListItemResponse
import ir.mehdibahrami.mbmovie.model.net.MoviesListResponse
import ir.mehdibahrami.mbmovie.model.repository.HomeRepository
import ir.mehdibahrami.mbmovie.paging.DefaultPaginator
import ir.mehdibahrami.mbmovie.paging.ScreenState
import ir.mehdibahrami.mbmovie.util.NetworkChecker
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val networkChecker: NetworkChecker, private val repository: HomeRepository
) : ViewModel() {

    var topMoviesState by mutableStateOf(emptyList<MoviesListResponse.Data>())
    var genresListState by mutableStateOf(emptyList<GenresListItemResponse>())
    var allMoviesState by mutableStateOf(ScreenState())
    var isOnline by mutableStateOf(networkChecker.isNetConnected())
    var isOnlineForLoadNext by mutableStateOf(networkChecker.isNetConnected())


    private val allMoviesPaginator =
        DefaultPaginator(initialKey = allMoviesState.page, onLoadUpdated = {
            allMoviesState = allMoviesState.copy(isLoading = it)
        }, onRequest = { nextPage ->
            repository.getAllMovies(nextPage)
        }, getNextKey = {
            allMoviesState.page + 1
        }, onError = {
            allMoviesState = allMoviesState.copy(error = it?.localizedMessage)
        }, onSuccess = { items, newPage ->
            allMoviesState = allMoviesState.copy(
                items = allMoviesState.items + items, page = newPage, endReached = items.isEmpty()
            )
        })

    init {
        initLoad()
    }

    fun initLoad() {
        if (networkChecker.isNetConnected()) {
            isOnline = true
            isOnlineForLoadNext = true
            loadTopMovies()
            loadGenreList()
            loadFirstItems()
        } else {
            isOnline = false
        }
    }

    private fun loadTopMovies() {
        viewModelScope.launch {
            topMoviesState = repository.getTopMovies()
        }
    }

    private fun loadGenreList() {
        viewModelScope.launch {
            genresListState=repository.getGenreList()
        }
    }

    private fun loadFirstItems() {
        viewModelScope.launch {
            allMoviesPaginator.loadNextItems()
        }
    }

    fun loadNextItems() {
        if (networkChecker.isNetConnected()) {
            isOnlineForLoadNext = true
            viewModelScope.launch {
                allMoviesPaginator.loadNextItems()
            }
        } else {
            isOnlineForLoadNext = false
        }
    }
}