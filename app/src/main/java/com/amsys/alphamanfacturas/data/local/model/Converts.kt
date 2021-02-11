package com.amsys.alphamanfacturas.data.local.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


open class Converts {
    @TypeConverter
    fun listStringJson(value: Array<String>): String = Gson().toJson(value)

    @TypeConverter
    fun jsonStringList(value: String) = Gson().fromJson(value, Array<String>::class.java)

//    @TypeConverter
//    fun listIntJson(value: List<Int>): String = Gson().toJson(value)
//
//    @TypeConverter
//    fun jsonIntList(value: String): List<Int> =
//        Gson().fromJson(value, object : TypeToken<List<Int>>() {}.type)

    @TypeConverter
    fun listIntJson(value: Array<Int>): String = Gson().toJson(value)

    @TypeConverter
    fun jsonIntList(value: String) = Gson().fromJson(value, Array<Int>::class.java)
}