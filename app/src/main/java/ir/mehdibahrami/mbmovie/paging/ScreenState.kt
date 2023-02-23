package ir.mehdibahrami.mbmovie.paging

import ir.mehdibahrami.mbmovie.model.net.MoviesListResponse

data class ScreenState(
    val isLoading: Boolean = false,
    val items: List<MoviesListResponse.Data> = emptyList(),
    val error: String? = null,
    val endReached: Boolean = false,
    val page: Int = 1
)