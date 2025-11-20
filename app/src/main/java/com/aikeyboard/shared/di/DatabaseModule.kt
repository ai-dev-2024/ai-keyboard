package com.aikeyboard.shared.di

import android.content.Context
import androidx.room.Room
import com.aikeyboard.shared.data.database.AIKeyboardDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AIKeyboardDatabase {
        return Room.databaseBuilder(
            context,
            AIKeyboardDatabase::class.java,
            "aikeyboard.db"
        ).build()
    }

    @Provides
    fun provideDictionaryDao(database: AIKeyboardDatabase) = database.dictionaryDao()

    @Provides
    fun provideClipboardDao(database: AIKeyboardDatabase) = database.clipboardDao()
}

