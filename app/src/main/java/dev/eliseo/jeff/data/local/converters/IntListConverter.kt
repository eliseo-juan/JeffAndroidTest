package dev.eliseo.jeff.data.local.converters

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types
import dev.eliseo.jeff.util.JsonUtils

    class IntListTypeConverter {

    val adapter: JsonAdapter<List<Int>> = JsonUtils.moshi.adapter(Types.newParameterizedType(List::class.java, Integer::class.java))

    @TypeConverter
    fun getDBValue(model: List<Int>?): String? {
        return model?.let {
            adapter.toJson(it)
        }
    }

    @TypeConverter
    fun getModelValue(data: String?): List<Int>? {
        return data?.let {
            adapter.fromJson(it)
        }
    }
}