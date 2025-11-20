package com.aikeyboard.billing

import com.aikeyboard.shared.data.preferences.PreferencesManager
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PremiumFeaturesManager @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val billingManager: BillingManager
) {
    val isPremium: Flow<Boolean> = combine(
        preferencesManager.isPremium,
        billingManager.purchaseState
    ) { storedPremium, purchaseState ->
        storedPremium || (purchaseState is PurchaseState.Purchased && 
            billingManager.isProPurchased((purchaseState as PurchaseState.Purchased).purchases))
    }.distinctUntilChanged()
    
    suspend fun checkPremiumStatus() {
        val purchases = billingManager.queryPurchases()
        val hasPro = billingManager.isProPurchased(purchases)
        if (hasPro) {
            preferencesManager.setPremiumStatus(true)
        }
    }
    
    fun hasPremiumAccess(): Boolean {
        val purchaseState = billingManager.purchaseState.value
        return when (purchaseState) {
            is PurchaseState.Purchased -> {
                billingManager.isProPurchased(purchaseState.purchases)
            }
            else -> false
        }
    }
}

