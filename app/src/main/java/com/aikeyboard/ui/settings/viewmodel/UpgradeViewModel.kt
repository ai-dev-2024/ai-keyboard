package com.aikeyboard.ui.settings.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aikeyboard.billing.BillingConstants
import com.aikeyboard.billing.BillingManager
import com.aikeyboard.billing.PremiumFeaturesManager
import com.aikeyboard.billing.PurchaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpgradeViewModel @Inject constructor(
    private val premiumFeaturesManager: PremiumFeaturesManager,
    private val billingManager: BillingManager
) : ViewModel() {
    
    val isPremium: StateFlow<Boolean> = premiumFeaturesManager.isPremium
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
    
    val purchaseState: StateFlow<PurchaseState> = billingManager.purchaseState
    
    val billingConnectionState = billingManager.billingConnectionState
    
    init {
        // Observe purchase state changes and update premium status
        viewModelScope.launch {
            billingManager.purchaseState.collect { state ->
                if (state is PurchaseState.Purchased) {
                    checkPremiumStatus()
                }
            }
        }
    }
    
    fun checkPremiumStatus() {
        viewModelScope.launch {
            premiumFeaturesManager.checkPremiumStatus()
        }
    }
    
    fun launchPurchaseFlow(activity: Activity, skuId: String) {
        viewModelScope.launch {
            billingManager.launchPurchaseFlow(activity, skuId)
        }
    }
    
    fun restorePurchases() {
        viewModelScope.launch {
            billingManager.queryPurchases()
            checkPremiumStatus()
        }
    }
}

