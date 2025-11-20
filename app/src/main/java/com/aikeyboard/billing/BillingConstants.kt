package com.aikeyboard.billing

object BillingConstants {
    // Product ID for Pro unlock (managed product - one-time purchase)
    const val SKU_PRO_UNLOCK = "pro_unlock"
    
    // Product type
    const val PRODUCT_TYPE_INAPP = "inapp"
    
    // Purchase states
    const val PURCHASE_STATE_PURCHASED = 1
    const val PURCHASE_STATE_PENDING = 2
    
    // Acknowledge states
    const val ACKNOWLEDGE_STATE_ACKNOWLEDGED = 1
    const val ACKNOWLEDGE_STATE_UNACKNOWLEDGED = 0
}

