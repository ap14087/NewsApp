package NewsRepository

import API.instanceOfRetrofit
import db.ArticleDatabase
import model.Article

class Repository(
    val db: ArticleDatabase
) {
    suspend fun getBreakingnews(countryCode: String, pageNumber: Int) =
        instanceOfRetrofit.api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchNews(searchQuery: String , pageNumber : Int) =
        instanceOfRetrofit.api.searchForNews(searchQuery,pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}