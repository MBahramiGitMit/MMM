package ir.mehdibahrami.mbmovie.model.net


import com.google.gson.annotations.SerializedName

data class GenresListItemResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)
