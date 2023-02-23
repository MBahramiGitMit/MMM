package ir.mehdibahrami.mbmovie.screen.ganre

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mehdibahrami.mbmovie.model.net.MoviesListResponse
import ir.mehdibahrami.mbmovie.model.repository.GenreRepository
import ir.mehdibahrami.mbmovie.paging.DefaultPaginator
import ir.mehdibahrami.mbmovie.paging.ScreenState
import ir.mehdibahrami.mbmovie.util.NetworkChecker
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(
    private val networkChecker: NetworkChecker,
    private val repository: GenreRepository
) : ViewModel() {

    private var genreId: Int = 0

    var allMoviesByGenreState by mutableStateOf(ScreenState())
    var isOnline by mutableStateOf(networkChecker.isNetConnected())
    var isOnlineForLoadNext by mutableStateOf(networkChecker.isNetConnected())

    private var allMoviesByGenrePaginator: DefaultPaginator<Int, MoviesListResponse.Data>? = null

    fun initLoad(genreId: Int) {
        this.genreId = genreId
        refreshPaginator()
        loadFirstItems()
    }

    private fun refreshPaginator() {

        allMoviesByGenreState = ScreenState()

        allMoviesByGenrePaginator = DefaultPaginator(
            initialKey = allMoviesByGenreState.page,
            onLoadUpdated = {
                allMoviesByGenreState = allMoviesByGenreState.copy(isLoading = it)
            },
            onRequest = { nextPage ->
                repository.getMovieByGenre(genreId = genreId, page = nextPage)
            },
            getNextKey = {
                allMoviesByGenreState.page + 1
            },
            onError = {
                allMoviesByGenreState = allMoviesByGenreState.copy(error = it?.localizedMessage)
            },
            onSuccess = { items, newPage ->
                allMoviesByGenreState = allMoviesByGenreState.copy(
                    items = allMoviesByGenreState.items + items,
                    page = newPage,
                    endReached = items.isEmpty()
                )
            }
        )
    }

    fun loadFirstItems() {
        if (networkChecker.isNetConnected()) {
            isOnline = true
            isOnlineForLoadNext=true
            viewModelScope.launch {
                refreshPaginator()
                allMoviesByGenrePaginator?.loadNextItems()
            }
        } else {
            isOnline = false
        }
    }

    fun loadNextItems() {
        if (networkChecker.isNetConnected()) {
            isOnlineForLoadNext = true
            viewModelScope.launch {
                allMoviesByGenrePaginator?.loadNextItems()
            }
        } else {
            isOnlineForLoadNext = false
        }
    }
}