package ir.mehdibahrami.mbmovie.model.repository

import android.util.Log
import ir.mehdibahrami.mbmovie.model.net.ApiService
import ir.mehdibahrami.mbmovie.model.net.MoviesListResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreRepository @Inject constructor(private val apiService: ApiService) {
    private var pageCount = 1
    private var lastId = 0

    suspend fun getMovieByGenre(genreId: Int, page: Int): Result<List<MoviesListResponse.Data>> {

        if (genreId != lastId) pageCount = 1

        var result = Result.success(emptyList<MoviesListResponse.Data>())

        if (page >= pageCount) {
            val response = apiService.getMoviesByGenre(genreId = genreId, page = page)
            if (response.isSuccessful) {
                result = Result.success(response.body()?.data!!)
                pageCount = response.body()?.metadata?.pageCount!!.toInt()
            } else {
                Log.e("7575", "searchMoviesByName: SearchRepository")
            }
        }

        return result
    }
}