package com.vivek.marksapp.di

import com.vivek.marksapp.data.json.JsonParser
import com.vivek.marksapp.data.json.QuestionListingsParser
import com.vivek.marksapp.data.repository.QuestionCacheImpl
import com.vivek.marksapp.domain.model.QuestionListing
import com.vivek.marksapp.domain.repository.QuestionCache
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindQuestionCache(
        questionCacheImpl: QuestionCacheImpl
    ): QuestionCache

    @Singleton
    @Binds
    abstract fun bindQuestionListingParser(
        questionListingsParser: QuestionListingsParser
    ): JsonParser<QuestionListing>
}




















