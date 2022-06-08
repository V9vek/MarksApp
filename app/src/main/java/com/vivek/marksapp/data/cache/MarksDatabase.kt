package com.vivek.marksapp.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vivek.marksapp.data.cache.model.QuestionListingEntity
import com.vivek.marksapp.data.cache.model.typeconverter.QuestionListingEntityConverter

@Database(
    entities = [QuestionListingEntity::class],
    version = 1
)
@TypeConverters(QuestionListingEntityConverter::class)
abstract class MarksDatabase : RoomDatabase() {

    abstract val dao: MarksDao

    companion object {
        const val DATABASE_NAME = "marksdb.db"
    }
}















