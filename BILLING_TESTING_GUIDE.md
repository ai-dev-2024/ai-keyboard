# AI Keyboard — Billing Testing Guide

## Overview

This guide covers testing the Google Play Billing implementation for AI Keyboard's Pro unlock feature.

## Prerequisites

1. **Google Play Console Setup:**
   - App published to Internal/Alpha/Beta track
   - In-app product `pro_unlock` created and active
   - License testing accounts configured

2. **Test Accounts:**
   - At least 2 Google accounts added to license testing
   - Accounts should be on test device

3. **Build Configuration:**
   - Release build with ProGuard enabled
   - Signed with release keystore
   - Billing library version: 6.1.0

## Testing Checklist

### 1. Billing Client Connection
- [ ] Billing client connects successfully
- [ ] Connection state updates correctly
- [ ] Handles connection failures gracefully
- [ ] Reconnects automatically after network issues

**Test Steps:**
1. Open app
2. Navigate to Settings > Upgrade
3. Verify "Loading..." or purchase button appears (not error)
4. Check Logcat for connection success

**Expected Logcat:**
```
BillingManager: Billing client connected successfully
```

---

### 2. Product Details Loading
- [ ] Product details load correctly
- [ ] Price displays in local currency
- [ ] Product name and description correct
- [ ] Handles missing product gracefully

**Test Steps:**
1. Open Upgrade screen
2. Wait for product details to load
3. Verify price displays correctly
4. Verify product name matches Play Console

**Expected Result:**
- Price shown in device's currency
- Product name: "AI Keyboard Pro" (or as configured)
- No error messages

---

### 3. Purchase Flow
- [ ] Purchase button initiates flow
- [ ] Google Play purchase dialog appears
- [ ] Purchase completes successfully
- [ ] Purchase is acknowledged
- [ ] Premium status updates immediately
- [ ] UI reflects premium status

**Test Steps:**
1. Tap "Upgrade to Pro" button
2. Complete purchase in Google Play dialog
3. Verify purchase success message
4. Check that premium features are unlocked
5. Restart app and verify premium status persists

**Expected Result:**
- Purchase dialog appears
- After purchase, premium badge shows
- Premium features accessible
- Status persists after app restart

---

### 4. Purchase Cancellation
- [ ] User can cancel purchase
- [ ] App handles cancellation gracefully
- [ ] No premium access granted
- [ ] UI returns to normal state

**Test Steps:**
1. Tap "Upgrade to Pro"
2. Cancel in Google Play dialog
3. Verify app returns to normal state
4. Verify no premium access granted

**Expected Result:**
- No error messages
- App returns to upgrade screen
- Premium features remain locked

---

### 5. Restore Purchases
- [ ] "Restore Purchases" button works
- [ ] Previously purchased status restored
- [ ] Premium features unlock after restore
- [ ] Works across app reinstalls

**Test Steps:**
1. Purchase Pro on Device A
2. Uninstall app
3. Reinstall app
4. Tap "Restore Purchases"
5. Verify premium status restored

**Expected Result:**
- Premium status restored
- Premium features accessible
- No need to purchase again

---

### 6. Already Purchased State
- [ ] App recognizes existing purchase
- [ ] Premium badge shows immediately
- [ ] No purchase button for existing users
- [ ] "You're a Pro user" message displays

**Test Steps:**
1. Purchase Pro
2. Close and reopen app
3. Navigate to Upgrade screen
4. Verify premium badge and message

**Expected Result:**
- Premium badge visible
- "You're a Pro user" message
- No purchase button

---

### 7. Network Error Handling
- [ ] Handles network errors gracefully
- [ ] Shows appropriate error message
- [ ] Retries connection automatically
- [ ] Doesn't crash on network issues

**Test Steps:**
1. Enable airplane mode
2. Open Upgrade screen
3. Verify error handling
4. Disable airplane mode
5. Verify reconnection

**Expected Result:**
- Error message shown (not crash)
- Reconnects when network available
- Purchase flow works after reconnection

---

### 8. Purchase Validation
- [ ] Purchases are validated server-side (if implemented)
- [ ] Invalid purchases are rejected
- [ ] Purchase state updates correctly

**Test Steps:**
1. Complete a purchase
2. Check Logcat for validation
3. Verify purchase state updates

**Expected Logcat:**
```
BillingManager: Purchase validated
PremiumFeaturesManager: Premium status updated: true
```

---

### 9. Premium Feature Gating
- [ ] Premium features check status correctly
- [ ] Free users see upgrade prompts
- [ ] Premium users access all features
- [ ] Status updates reactively

**Test Features to Test:**
- [ ] Premium themes
- [ ] Unlimited clipboard history
- [ ] Custom theme export/import
- [ ] Any other premium features

**Test Steps:**
1. As free user, try to access premium feature
2. Verify upgrade prompt appears
3. Purchase Pro
4. Verify feature accessible
5. Restart app
6. Verify feature still accessible

---

### 10. Edge Cases
- [ ] App handles purchase during app backgrounding
- [ ] Purchase state persists after device restart
- [ ] Multiple purchase attempts handled correctly
- [ ] Purchase acknowledgment retries on failure

**Test Steps:**
1. Start purchase flow
2. Background app during purchase
3. Complete purchase
4. Return to app
5. Verify purchase processed

---

## License Testing Accounts

### Setting Up Test Accounts

1. Go to Google Play Console
2. Navigate to **Setup > License testing**
3. Add test accounts (Gmail addresses)
4. Save changes

### Using Test Accounts

- Test accounts can purchase without real payment
- Purchases are free for test accounts
- Test purchases don't appear in production
- Use test accounts for all testing

---

## Logcat Monitoring

### Enable Billing Logs
```kotlin
// In BillingManager.kt
private const val TAG = "BillingManager"
Log.d(TAG, "Billing client connected")
```

### Key Log Tags
- `BillingManager` - Billing operations
- `PremiumFeaturesManager` - Premium status updates
- `UpgradeViewModel` - UI state changes

### Logcat Filters
```bash
# Billing logs only
adb logcat -s BillingManager:D PremiumFeaturesManager:D

# All billing-related logs
adb logcat | grep -E "Billing|Purchase|Premium"

# Errors only
adb logcat *:E | grep -E "Billing|Purchase"
```

---

## Common Issues & Solutions

### Issue: Billing client doesn't connect
**Solution:**
- Verify app is signed with release keystore
- Check Google Play Console product is active
- Verify app is published to at least Internal track
- Check device has Google Play Services

### Issue: Purchase doesn't complete
**Solution:**
- Verify test account is added to license testing
- Check product ID matches (`pro_unlock`)
- Verify product is active in Play Console
- Check network connection

### Issue: Premium status doesn't persist
**Solution:**
- Check `PreferencesManager` implementation
- Verify DataStore is working
- Check purchase acknowledgment
- Verify `PremiumFeaturesManager` logic

### Issue: Restore purchases doesn't work
**Solution:**
- Verify `queryPurchases()` is called
- Check purchase state flow
- Verify purchase validation logic
- Test with fresh install

---

## Testing Report Template

```
Billing Test Report - AI Keyboard v1.0.0
Date: [DATE]
Tester: [NAME]
Device: [MODEL]
Android Version: [VERSION]
Play Services Version: [VERSION]

Test Results:
1. Billing Connection: ✅/❌
2. Product Details: ✅/❌
3. Purchase Flow: ✅/❌
4. Purchase Cancellation: ✅/❌
5. Restore Purchases: ✅/❌
6. Already Purchased: ✅/❌
7. Network Errors: ✅/❌
8. Purchase Validation: ✅/❌
9. Premium Gating: ✅/❌
10. Edge Cases: ✅/❌

Issues Found:
- [LIST ISSUES]

Screenshots:
- [ATTACH SCREENSHOTS]

Notes:
- [ADDITIONAL NOTES]
```

---

## Pre-Release Checklist

- [ ] All test cases pass
- [ ] Tested on multiple devices
- [ ] Tested with multiple accounts
- [ ] Restore purchases verified
- [ ] Premium features gating verified
- [ ] Error handling tested
- [ ] No crashes during purchase flow
- [ ] Purchase state persists correctly
- [ ] Product ID matches Play Console
- [ ] Price displays correctly in all regions

---

## Production Checklist

Before enabling in production:
- [ ] Remove all test accounts from license testing
- [ ] Verify product is active
- [ ] Set appropriate price
- [ ] Test with real purchase (small amount)
- [ ] Verify purchase appears in Play Console
- [ ] Monitor for purchase issues
- [ ] Set up purchase analytics (if applicable)

