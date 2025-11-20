package com.aikeyboard.shared.di

import com.aikeyboard.ime.autocorrect.AutoCorrectEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AutoCorrectModule {
    @Provides
    @Singleton
    fun provideAutoCorrectEngine(
        dictionaryDao: com.aikeyboard.dictionary.data.DictionaryDao
    ): AutoCorrectEngine {
        return AutoCorrectEngine(dictionaryDao)
    }
}

