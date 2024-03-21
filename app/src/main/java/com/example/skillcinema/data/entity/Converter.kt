package com.example.skillcinema.data.entity

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson


object Converter {
    @TypeConverter
    fun fromIntegerList(integerList: List<Int?>): String {
        val sb = StringBuilder()
        for (integer in integerList) {
            sb.append(integer).append(",")
        }
        return sb.toString()
    }

    @TypeConverter
    fun toIntegerList(data: String): List<Int> {
        val splitData = data.split(",".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val integerList: MutableList<Int> = ArrayList()
        for (s in splitData) {
            integerList.add(s.toInt())
        }
        return integerList
    }

    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Gson().toJson(list)
    }

}
