package com.aikeyboard.billing

import com.aikeyboard.shared.data.preferences.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Unit tests for PremiumFeaturesManager.
 * 
 * Note: These tests verify the premium status management logic.
 * Full integration testing should be done with Google Play Console license testing.
 */
class PremiumFeaturesManagerTest {
    
    @Mock
    private lateinit var preferencesManager: PreferencesManager
    
    @Mock
    private lateinit var billingManager: BillingManager
    
    private lateinit var premiumFeaturesManager: PremiumFeaturesManager
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        premiumFeaturesManager = PremiumFeaturesManager(preferencesManager, billingManager)
    }
    
    @Test
    fun `hasPremiumAccess returns true when Pro purchase exists`() = runTest {
        val mockPurchase = createMockPurchase(
            sku = BillingConstants.SKU_PRO_UNLOCK,
            purchaseState = com.android.billingclient.api.Purchase.PurchaseState.PURCHASED
        )
        
        val purchaseStateFlow = MutableStateFlow<PurchaseState>(
            PurchaseState.Purchased(listOf(mockPurchase))
        )
        `when`(billingManager.purchaseState).thenReturn(purchaseStateFlow)
        `when`(billingManager.isProPurchased(listOf(mockPurchase))).thenReturn(true)
        
        val result = premiumFeaturesManager.hasPremiumAccess()
        
        assertTrue("Should return true when Pro purchase exists", result)
    }
    
    @Test
    fun `hasPremiumAccess returns false when no Pro purchase exists`() = runTest {
        val purchaseStateFlow = MutableStateFlow<PurchaseState>(PurchaseState.NotPurchased)
        `when`(billingManager.purchaseState).thenReturn(purchaseStateFlow)
        
        val result = premiumFeaturesManager.hasPremiumAccess()
        
        assertFalse("Should return false when no Pro purchase exists", result)
    }
    
    @Test
    fun `hasPremiumAccess returns false when purchase is pending`() = runTest {
        val purchaseStateFlow = MutableStateFlow<PurchaseState>(PurchaseState.Pending)
        `when`(billingManager.purchaseState).thenReturn(purchaseStateFlow)
        
        val result = premiumFeaturesManager.hasPremiumAccess()
        
        assertFalse("Should return false when purchase is pending", result)
    }
    
    @Test
    fun `checkPremiumStatus updates preferences when Pro purchase exists`() = runTest {
        val mockPurchase = createMockPurchase(
            sku = BillingConstants.SKU_PRO_UNLOCK,
            purchaseState = com.android.billingclient.api.Purchase.PurchaseState.PURCHASED
        )
        
        `when`(billingManager.queryPurchases()).thenReturn(listOf(mockPurchase))
        `when`(billingManager.isProPurchased(listOf(mockPurchase))).thenReturn(true)
        
        premiumFeaturesManager.checkPremiumStatus()
        
        verify(preferencesManager, times(1)).setPremiumStatus(true)
    }
    
    @Test
    fun `checkPremiumStatus does not update preferences when no Pro purchase`() = runTest {
        `when`(billingManager.queryPurchases()).thenReturn(emptyList())
        `when`(billingManager.isProPurchased(emptyList())).thenReturn(false)
        
        premiumFeaturesManager.checkPremiumStatus()
        
        verify(preferencesManager, never()).setPremiumStatus(anyBoolean())
    }
    
    // Helper function to create mock purchase
    private fun createMockPurchase(
        sku: String,
        purchaseState: com.android.billingclient.api.Purchase.PurchaseState
    ): com.android.billingclient.api.Purchase {
        val purchaseJson = """
        {
            "orderId": "test_order_${System.currentTimeMillis()}",
            "packageName": "com.aikeyboard",
            "productIds": ["$sku"],
            "purchaseTime": ${System.currentTimeMillis()},
            "purchaseState": ${purchaseState.ordinal},
            "purchaseToken": "test_token_${System.currentTimeMillis()}",
            "autoRenewing": false
        }
        """.trimIndent()
        
        return com.android.billingclient.api.Purchase(purchaseJson, "test_signature")
    }
}

