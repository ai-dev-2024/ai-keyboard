package com.aikeyboard.shared.di

import android.content.Context
import com.aikeyboard.billing.BillingManager
import com.aikeyboard.billing.PremiumFeaturesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BillingModule {
    @Provides
    @Singleton
    fun provideBillingManager(@ApplicationContext context: Context): BillingManager {
        // Don't initialize immediately - let it initialize lazily when needed
        // This prevents crashes on devices without Google Play Services
        return try {
            BillingManager(context)
        } catch (e: Exception) {
            android.util.Log.e("BillingModule", "Failed to create BillingManager", e)
            // Return a manager that will gracefully handle errors
            BillingManager(context)
        }
    }
    
    @Provides
    @Singleton
    fun providePremiumFeaturesManager(
        preferencesManager: com.aikeyboard.shared.data.preferences.PreferencesManager,
        billingManager: BillingManager
    ): PremiumFeaturesManager {
        return PremiumFeaturesManager(preferencesManager, billingManager)
    }
}

