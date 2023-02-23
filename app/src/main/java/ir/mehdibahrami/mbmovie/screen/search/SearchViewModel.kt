package ir.mehdibahrami.mbmovie.screen.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mehdibahrami.mbmovie.model.net.MoviesListResponse
import ir.mehdibahrami.mbmovie.model.repository.SearchRepository
import ir.mehdibahrami.mbmovie.paging.DefaultPaginator
import ir.mehdibahrami.mbmovie.paging.ScreenState
import ir.mehdibahrami.mbmovie.util.NetworkChecker
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val networkChecker: NetworkChecker, private val repository: SearchRepository
) : ViewModel() {

    var searchedMoviesScreenState by mutableStateOf(ScreenState())
    var searchFieldValueState by mutableStateOf("")
    private var searchMoviePaginator: DefaultPaginator<Int, MoviesListResponse.Data>? = null

    var isOnline by mutableStateOf(networkChecker.isNetConnected())
    var isOnlineForLoadNext by mutableStateOf(networkChecker.isNetConnected())

    private var searchJob: Job? = null
    fun onSearchFieldValueChange(newValue: String) {
        searchFieldValueState = newValue
        searchJob?.cancel()
        loadFirstItems()
    }

    fun loadFirstItems() {
        if (networkChecker.isNetConnected()) {
            isOnline = true
            isOnlineForLoadNext=true
            searchJob = viewModelScope.launch {
                delay(500L)
                refreshPaginator()
                searchMoviePaginator?.loadNextItems()
            }
        } else {
            isOnline = false
        }
    }

    fun loadNextItems() {
        if (networkChecker.isNetConnected()) {
            isOnlineForLoadNext = true
            viewModelScope.launch {
                searchMoviePaginator?.loadNextItems()
            }
        } else {
            isOnlineForLoadNext = false
        }
    }

    private fun refreshPaginator() {
        searchedMoviesScreenState = ScreenState()

        searchMoviePaginator =
            DefaultPaginator(initialKey = searchedMoviesScreenState.page, onLoadUpdated = {
                searchedMoviesScreenState = searchedMoviesScreenState.copy(isLoading = it)
            }, onRequest = { nextPage ->
                repository.searchMoviesByName(searchFieldValueState, nextPage)
            }, getNextKey = {
                searchedMoviesScreenState.page + 1
            }, onError = {
                searchedMoviesScreenState =
                    searchedMoviesScreenState.copy(error = it?.localizedMessage)
            }, onSuccess = { items, newPage ->
                searchedMoviesScreenState = searchedMoviesScreenState.copy(
                    items = searchedMoviesScreenState.items + items,
                    page = newPage,
                    endReached = items.isEmpty()
                )
            })
    }

}