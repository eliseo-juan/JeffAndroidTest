package dev.eliseo.jeff.data.remote

import dev.eliseo.jeff.BuildConfig
import dev.eliseo.jeff.data.remote.util.LiveDataCallAdapterFactory
import dev.eliseo.jeff.util.JsonUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Server {

    private const val baseUrl = "http://api.geonames.org"
    val api: API

    init {
        val client = OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                this.level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(JsonUtils.moshiFactory)
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()

        api = retrofit.create(
            API::class.java)
    }
}