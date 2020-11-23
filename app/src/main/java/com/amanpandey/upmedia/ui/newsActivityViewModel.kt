package com.amanpandey.upmedia.ui

import NewsRepository.Repository
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amanpandey.upmedia.NewsMain
import kotlinx.coroutines.launch
import model.Article
import model.NewsResponse
import retrofit2.Response
import utilPackage.ResourceUsed
import java.io.IOException

class newsActivityViewModel(
    app: Application,
    val newsRepository: Repository
) : AndroidViewModel(app) {
    val breakingNews: MutableLiveData<ResourceUsed<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse?= null

    val searchNews: MutableLiveData<ResourceUsed<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse?= null

    init {
        getBreakingNews("id")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)

    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : ResourceUsed<NewsResponse> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                breakingNewsPage += 1
                if(breakingNewsResponse == null){
                    breakingNewsResponse = resultResponse
                }else{
                    val oldArticle = breakingNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return ResourceUsed.Success(breakingNewsResponse ?: resultResponse)
            }
        }

        return ResourceUsed.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : ResourceUsed<NewsResponse> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                searchNewsPage += 1
                if(searchNewsResponse == null){
                    searchNewsResponse = resultResponse
                }else{
                    val oldArticle = searchNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return ResourceUsed.Success(searchNewsResponse ?: resultResponse)
            }
        }

        return ResourceUsed.Error(response.message())
    }

    fun savedArticle(article: Article) = viewModelScope.launch{
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    private suspend fun safeSearchNewsCall(searchQuery : String){
        searchNews.postValue(ResourceUsed.Loading())
        try{
            if (hasInternetConnection()) {
                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            }else{
                searchNews.postValue(ResourceUsed.Error("No Internet Connection"))
            }
        }catch (t : Throwable){
            when(t){
                is IOException -> searchNews.postValue(ResourceUsed.Error("Network Failure"))
                else -> searchNews.postValue(ResourceUsed.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeBreakingNewsCall(countryCode : String){
        breakingNews.postValue(ResourceUsed.Loading())
        try{
            if (hasInternetConnection()) {
                val response = newsRepository.getBreakingnews(countryCode, breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            }else{
                breakingNews.postValue(ResourceUsed.Error("No Internet Connection"))
            }
        }catch (t : Throwable){
            when(t){
                is IOException -> breakingNews.postValue(ResourceUsed.Error("Network Failure"))
                else -> breakingNews.postValue(ResourceUsed.Error("Conversion Error"))
            }
        }
    }

    private fun hasInternetConnection(): Boolean{
        val connectivityManager = getApplication<NewsMain>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }

        return false
    }
}
