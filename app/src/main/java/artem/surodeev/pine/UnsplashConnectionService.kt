package artem.surodeev.pine

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashConnectionService {
    @GET("/photos?per_page=20&order_by=popular")
    fun getImages(@Query("client_id") id: String, @Query("page") page: Int): Call<ArrayList<Image>>

    @GET("/search/photos?per_page=20")
    fun getImagesByKeyword(@Query("query") query: String, @Query("client_id") id: String, @Query("page") page: Int): Call<SearchImage>
}