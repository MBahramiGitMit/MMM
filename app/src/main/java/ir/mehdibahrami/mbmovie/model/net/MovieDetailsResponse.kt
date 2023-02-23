package ir.mehdibahrami.mbmovie.model.net


import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(
    @SerializedName("actors")
    val actors: String? = null,
    @SerializedName("awards")
    val awards: String? = null,
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("director")
    val director: String? = null,
    @SerializedName("genres")
    val genres: List<String?> = emptyList(),
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("images")
    val images: List<String?>? = emptyList(),
    @SerializedName("imdb_id")
    val imdbId: String? = null,
    @SerializedName("imdb_rating")
    val imdbRating: String? = null,
    @SerializedName("imdb_votes")
    val imdbVotes: String? = null,
    @SerializedName("metascore")
    val metascore: String? = null,
    @SerializedName("plot")
    val plot: String? = null,
    @SerializedName("poster")
    val poster: String? = null,
    @SerializedName("rated")
    val rated: String? = null,
    @SerializedName("released")
    val released: String? = null,
    @SerializedName("runtime")
    val runtime: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("writer")
    val writer: String? = null,
    @SerializedName("year")
    val year: String? = null
)