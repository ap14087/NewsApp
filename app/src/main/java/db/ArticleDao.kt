package db

import androidx.lifecycle.LiveData
import androidx.room.*
import model.Article


@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    //This on Room query News API interface has Retofit Query
    //And the function will return live data so funcation can not be suspend
    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}