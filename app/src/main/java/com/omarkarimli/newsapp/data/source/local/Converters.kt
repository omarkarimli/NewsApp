package com.omarkarimli.newsapp.data.source.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.omarkarimli.newsapp.domain.models.Source

class Converters {

    // Convert Source object to a JSON string
    @TypeConverter
    fun fromSource(source: Source?): String? {
        return source?.let { Gson().toJson(it) }
    }

    // Convert JSON string to a Source object
    @TypeConverter
    fun toSource(sourceString: String?): Source? {
        return sourceString?.let {
            val type = object : TypeToken<Source>() {}.type
            Gson().fromJson(it, type)
        }
    }
}