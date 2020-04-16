package kz.iitu.iituboardandroid.api

import com.google.gson.GsonBuilder
import kz.iitu.iituboardandroid.BuildConfig
import kz.iitu.iituboardandroid.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class IituServiceManager {

    private val timeUnit = TimeUnit.SECONDS

    private val gson = GsonBuilder().create()

    fun createApi(): IituApi {

        val clientBuilder = getClientBuilder()
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constants.HOST)
            .client(clientBuilder.build())
            .build()

        return retrofit.create(IituApi::class.java)
    }


    private fun getClientBuilder(): OkHttpClient.Builder {

        val clientBuilder = OkHttpClient.Builder()

        clientBuilder
            .readTimeout(TIME_OUT_MEDIUM, timeUnit)
            .connectTimeout(TIME_OUT_MEDIUM, timeUnit)

        if (BuildConfig.DEBUG)
            clientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })

        return clientBuilder
    }

    companion object {
        private const val TIME_OUT_MEDIUM = 100L
    }
}