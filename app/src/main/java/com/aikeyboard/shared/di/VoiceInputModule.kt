package com.aikeyboard.shared.di

import android.content.Context
import com.aikeyboard.voiceinput.modelmanager.ModelManager
import com.aikeyboard.voiceinput.service.VoiceInputService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VoiceInputModule {
    @Provides
    @Singleton
    fun provideModelManager(@ApplicationContext context: Context): ModelManager {
        return ModelManager(context)
    }

    @Provides
    @Singleton
    fun provideVoiceInputService(
        @ApplicationContext context: Context,
        modelManager: ModelManager
    ): VoiceInputService {
        return VoiceInputService(context, modelManager)
    }
}

