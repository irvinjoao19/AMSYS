package com.amsys.alphamanfacturas.data.local.model

import androidx.room.TypeConverter
import com.google.gson.Gson

open class Converts {
    @TypeConverter
    fun listToJson(value: List<String>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()
}