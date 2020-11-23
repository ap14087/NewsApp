package utilPackage

class ConstantUsed {
    //when we create a companion object we do not the instance of the class to access it
    companion object{
        const val API_KEY = "a997cc93d06b4589b63a4d74542e3125"
        const val BASE_URL = "https://newsapi.org/"
        const val SEARCH_NEWS_TIME_DELAY = 500L
        const val QUERY_PAGE_SIZE = 20
    }
}