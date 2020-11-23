package API

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import utilPackage.ConstantUsed.Companion.BASE_URL

class instanceOfRetrofit {

    companion object{
        private val retrofit by lazy {
            //To log responses of retrofit coz it is very useful in debugging
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            //We use Interceptor to create a network client
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        }

        //actual api object that we will use from anywhere to make our network request
        val api by lazy {
            retrofit.create(NewsAPIInterface::class.java)
        }
    }
}