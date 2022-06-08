package com.vivek.marksapp.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.vivek.marksapp.MarksApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext context: Context): MarksApplication {
        return context as MarksApplication
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }
}


























