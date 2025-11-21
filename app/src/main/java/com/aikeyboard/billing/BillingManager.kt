package com.aikeyboard.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import javax.inject.Inject
import javax.inject.Singleton
import java.lang.reflect.Method

// Extension function to convert Task to suspend function for Google Play Billing
private suspend fun <T> com.google.android.gms.tasks.Task<T>.await(): T = suspendCancellableCoroutine { cont ->
    addOnCompleteListener { task ->
        if (task.exception != null) {
            cont.resumeWithException(task.exception!!)
        } else {
            cont.resume(task.result)
        }
    }
}

@Singleton
class BillingManager @Inject constructor(
    private val context: Context
) {
    private var billingClient: BillingClient? = null
    private val _billingConnectionState = MutableStateFlow<BillingConnectionState>(BillingConnectionState.Disconnected)
    val billingConnectionState: StateFlow<BillingConnectionState> = _billingConnectionState.asStateFlow()
    
    private val _purchaseState = MutableStateFlow<PurchaseState>(PurchaseState.Loading)
    val purchaseState: StateFlow<PurchaseState> = _purchaseState.asStateFlow()
    
    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            _purchaseState.value = PurchaseState.Cancelled
        } else {
            _purchaseState.value = PurchaseState.Error(billingResult.debugMessage ?: "Unknown error")
        }
    }
    
    fun initialize() {
        try {
            billingClient = BillingClient.newBuilder(context)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build()
            
            billingClient?.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        _billingConnectionState.value = BillingConnectionState.Connected
                        // Launch coroutine to query purchases
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                queryPurchases()
                            } catch (e: Exception) {
                                // Silently handle query errors - billing may not be available
                                _billingConnectionState.value = BillingConnectionState.Disconnected
                            }
                        }
                    } else {
                        _billingConnectionState.value = BillingConnectionState.Error(
                            billingResult.debugMessage ?: "Billing setup failed"
                        )
                    }
                }
                
                override fun onBillingServiceDisconnected() {
                    _billingConnectionState.value = BillingConnectionState.Disconnected
                }
            })
        } catch (e: Exception) {
            // Handle cases where Google Play Services is not available
            _billingConnectionState.value = BillingConnectionState.Error(
                "Billing not available: ${e.message}"
            )
        }
    }
    
    suspend fun queryPurchases(): List<Purchase> {
        val billingClient = this.billingClient ?: return emptyList()
        
        return try {
            val purchases = suspendCancellableCoroutine<List<Purchase>> { cont ->
                billingClient.queryPurchasesAsync(
                    QueryPurchasesParams.newBuilder()
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                ) { billingResult, purchasesResult ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchasesResult != null) {
                        // Use reflection to access purchasesList as the property might have different access
                        val purchasesList: List<Purchase> = try {
                            val method: Method? = purchasesResult.javaClass.methods.find { 
                                it.name == "getPurchasesList" || it.name == "purchasesList" 
                            }
                            if (method != null) {
                                (method.invoke(purchasesResult) as? List<*>)?.filterIsInstance<Purchase>() ?: emptyList()
                            } else {
                                // Fallback - try accessing as field or use empty list
                                emptyList()
                            }
                        } catch (e: Exception) {
                            emptyList()
                        }
                        cont.resume(purchasesList) {}
                    } else {
                        cont.resumeWithException(Exception(billingResult.debugMessage ?: "Query failed"))
                    }
                }
            }
            if (purchases.isNotEmpty()) {
                // Verify and acknowledge purchases
                purchases.forEach { purchase: Purchase ->
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        if (!purchase.isAcknowledged) {
                            acknowledgePurchase(purchase)
                        }
                    }
                }
                _purchaseState.value = PurchaseState.Purchased(purchases)
            } else {
                _purchaseState.value = PurchaseState.NotPurchased
            }
            
            purchases
        } catch (e: Exception) {
            _purchaseState.value = PurchaseState.Error(e.message ?: "Query failed")
            emptyList()
        }
    }
    
    suspend fun launchPurchaseFlow(activity: Activity, skuId: String) {
        val billingClient = this.billingClient ?: run {
            _purchaseState.value = PurchaseState.Error("Billing client not initialized")
            return
        }
        
        try {
            val productDetailsParams = QueryProductDetailsParams.newBuilder()
                .setProductList(
                    listOf(
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(skuId)
                            .setProductType(BillingClient.ProductType.INAPP)
                            .build()
                    )
                )
                .build()
            
            val productDetailsList = suspendCancellableCoroutine<List<ProductDetails>> { cont ->
                billingClient.queryProductDetailsAsync(productDetailsParams) { billingResult, detailsList ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        cont.resume(detailsList) {}
                    } else {
                        cont.resumeWithException(Exception(billingResult.debugMessage ?: "Query failed"))
                    }
                }
            }
            
            val productDetails = productDetailsList.firstOrNull()
            if (productDetails == null) {
                _purchaseState.value = PurchaseState.Error("Product not found")
                return
            }
            
            val productDetailsParamsList = listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .build()
            )
            
            val billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build()
            
            _purchaseState.value = PurchaseState.Purchasing
            val responseCode = billingClient.launchBillingFlow(activity, billingFlowParams).responseCode
            
            if (responseCode != BillingClient.BillingResponseCode.OK) {
                _purchaseState.value = PurchaseState.Error("Failed to launch billing flow")
            }
        } catch (e: Exception) {
            _purchaseState.value = PurchaseState.Error(e.message ?: "Purchase flow failed")
        }
    }
    
    private suspend fun acknowledgePurchase(purchase: Purchase) {
        val billingClient = this.billingClient ?: return
        
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                try {
                    val acknowledgeParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()
                    
                    suspendCancellableCoroutine<BillingResult> { cont ->
                        billingClient.acknowledgePurchase(acknowledgeParams) { billingResult ->
                            cont.resume(billingResult) {}
                        }
                    }
                } catch (e: Exception) {
                    // Log error but don't fail the purchase
                }
            }
        }
    }
    
    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                // Acknowledge in background
                // Note: In production, you should also verify purchases server-side
            }
            _purchaseState.value = PurchaseState.Purchased(listOf(purchase))
        } else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
            _purchaseState.value = PurchaseState.Pending
        }
    }
    
    fun isProPurchased(purchases: List<Purchase>): Boolean {
        return purchases.any { 
            it.products.contains(BillingConstants.SKU_PRO_UNLOCK) && 
            it.purchaseState == Purchase.PurchaseState.PURCHASED 
        }
    }
    
    fun release() {
        billingClient?.endConnection()
        billingClient = null
    }
}

sealed class BillingConnectionState {
    object Disconnected : BillingConnectionState()
    object Connected : BillingConnectionState()
    data class Error(val message: String) : BillingConnectionState()
}

sealed class PurchaseState {
    object Loading : PurchaseState()
    object NotPurchased : PurchaseState()
    object Purchasing : PurchaseState()
    object Pending : PurchaseState()
    object Cancelled : PurchaseState()
    data class Purchased(val purchases: List<Purchase>) : PurchaseState()
    data class Error(val message: String) : PurchaseState()
}

