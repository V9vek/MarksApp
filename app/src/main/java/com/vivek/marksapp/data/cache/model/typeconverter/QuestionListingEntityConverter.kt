package com.vivek.marksapp.data.cache.model.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vivek.marksapp.data.cache.model.OptionEntity

class QuestionListingEntityConverter {

    // List<String> <-> String

    @TypeConverter
    fun fromStringToListOfString(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromListOfStringToString(list: List<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    // List<Option> <-> String

    @TypeConverter
    fun fromStringToListOfOption(value: String): List<OptionEntity> {
        val type = object : TypeToken<List<OptionEntity>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromListOfOptionToString(list: List<OptionEntity>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}