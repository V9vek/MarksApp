package com.vivek.marksapp.di

import android.app.Application
import androidx.room.Room
import com.vivek.marksapp.data.cache.MarksDatabase
import com.vivek.marksapp.data.cache.MarksDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideDb(app: Application): MarksDatabase {
        return Room.databaseBuilder(
            app,
            MarksDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}





















