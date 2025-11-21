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
        return try {
            Room.databaseBuilder(
                context,
                AIKeyboardDatabase::class.java,
                "aikeyboard.db"
            )
            .fallbackToDestructiveMigration() // Prevent crashes on schema changes
            .build()
        } catch (e: Exception) {
            android.util.Log.e("DatabaseModule", "Failed to create database", e)
            // Return a fallback database instance
            Room.databaseBuilder(
                context,
                AIKeyboardDatabase::class.java,
                "aikeyboard_fallback.db"
            )
            .fallbackToDestructiveMigration()
            .build()
        }
    }

    @Provides
    fun provideDictionaryDao(database: AIKeyboardDatabase) = database.dictionaryDao()

    @Provides
    fun provideClipboardDao(database: AIKeyboardDatabase) = database.clipboardDao()
}












