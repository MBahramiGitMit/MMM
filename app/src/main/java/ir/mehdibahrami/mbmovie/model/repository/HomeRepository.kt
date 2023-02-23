package ir.mehdibahrami.mbmovie.model.repository

import android.util.Log
import ir.mehdibahrami.mbmovie.model.net.ApiService
import ir.mehdibahrami.mbmovie.model.net.GenresListItemResponse
import ir.mehdibahrami.mbmovie.model.net.MoviesListResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(private val apiService: ApiService) {

    private var pageCount: Int = 1

    suspend fun getAllMovies(page: Int): Result<List<MoviesListResponse.Data>> {

        var result = Result.success(emptyList<MoviesListResponse.Data>())

        if (page <= pageCount) {
            val response = apiService.getAllMovies(page)
            if (response.isSuccessful) {
                result = Result.success(response.body()?.data!!)
                pageCount = response.body()?.metadata?.pageCount!!
            } else {
                Log.e("7575", "getAllMovies: HomeRepository")
            }
        }
        return result
    }

    suspend fun getGenreList(): List<GenresListItemResponse> {
        val response = apiService.getAllGenres()
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            Log.e("7575", "loadGenreList: HomeRepository")
            emptyList()
        }
    }

    suspend fun getTopMovies(): List<MoviesListResponse.Data> {
        val response = apiService.getAllMovies((2..5).random())
        return if (response.isSuccessful) {
            response.body()?.data!!.subList(2, 7)
        } else {
            Log.e("7575", "getTopMovies: HomeRepository")
            emptyList()
        }
    }
}