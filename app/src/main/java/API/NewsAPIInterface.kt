package API

import model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import utilPackage.ConstantUsed.Companion.API_KEY

interface NewsAPIInterface {

    //Get Breaking news from our API making get request.passing end point of the base url to get the specific news
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        //Specify parameters we want to set from which country we are getting news.
        @Query("country")
        countryCode: String = "id",
        @Query("page") //we don't get all the articles we get 20 articles at a time and than refresh it
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        //Specify parameters we want to set from which country we are getting news.
        @Query("q")
        searchQuery: String,
        @Query("page") //we don't get all the articles we get 20 articles at a time and than refresh it
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>
}