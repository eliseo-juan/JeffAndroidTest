package dev.eliseo.jeff.util

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object JsonUtils {

    val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val moshiFactory: MoshiConverterFactory
        get() = MoshiConverterFactory.create(moshi)
}