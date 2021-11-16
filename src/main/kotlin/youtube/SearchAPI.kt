package youtube

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

data class SearchResult (
    @SerializedName("items")
    var searchList: List<SearchDTO> = arrayListOf()
)

data class SearchDTO(
    @SerializedName("id")
    var id: ID,
    @SerializedName("snippet")
    var snippet: Snippet?,
)

data class ID(
    var videoId: String?
)
data class Snippet(
    var title: String?,
    var description: String,
    var thumbnails: Thumbnail?
)
data class Thumbnail(
    var high: ThumbnailUrl?
)
data class ThumbnailUrl(
    var url: String?
)

interface SearchAPI {

    @GET("/youtube/v3/search")
    fun getInfo(
        @Query("key") key:String?,
        @Query("part") part:String?,
        @Query("maxResults") maxResults:String?,
        @Query("regionCode") regionCode:String?,
        @Query("q") q:String?,
    ): Call<SearchResult>
}
