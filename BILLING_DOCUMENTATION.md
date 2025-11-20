# AI Keyboard - Billing & Monetization Documentation

## Overview

AI Keyboard uses Google Play Billing Library v6+ to implement in-app purchases for premium features. The app follows a **Free app + Pro IAP unlock** model (Option B), allowing users to unlock premium features through a one-time purchase.

## Architecture

### Components

1. **BillingManager** (`com.aikeyboard.billing.BillingManager`)
   - Handles all Google Play Billing interactions
   - Manages BillingClient lifecycle
   - Processes purchases and acknowledgments
   - Provides purchase state flow

2. **PremiumFeaturesManager** (`com.aikeyboard.billing.PremiumFeaturesManager`)
   - Manages premium status across the app
   - Combines billing state with local preferences
   - Provides `isPremium` flow for reactive UI updates

3. **PreferencesManager** (`com.aikeyboard.shared.data.preferences.PreferencesManager`)
   - Stores premium status locally (encrypted via DataStore)
   - Persists premium status across app restarts

4. **UpgradeViewModel** (`com.aikeyboard.ui.settings.viewmodel.UpgradeViewModel`)
   - ViewModel for upgrade UI
   - Handles purchase flow initiation
   - Manages restore purchases functionality

## Product Configuration

### SKU Definition

- **Product ID**: `pro_unlock`
- **Product Type**: Managed Product (INAPP - one-time purchase)
- **Price**: Set in Google Play Console

### Setting Up in Google Play Console

1. Navigate to **Monetize > Products > In-app products**
2. Create a new product:
   - Product ID: `pro_unlock`
   - Name: "AI Keyboard Pro"
   - Description: "Unlock all premium features"
   - Price: Set your desired price
   - Status: Active

## Premium Features

The following features are gated behind the Pro unlock:

1. **Premium Themes**
   - Exclusive premium theme designs
   - Custom theme export/import functionality

2. **Unlimited Clipboard History**
   - No limits on clipboard entries
   - Advanced clipboard organization features

3. **Advanced ASR Model Settings**
   - Fine-tune voice recognition models
   - Advanced parameter configuration

4. **Priority Inference Mode**
   - Lower latency for voice recognition
   - Optimized processing pipeline

5. **Premium Waveform Animations**
   - Beautiful animated waveforms during voice input
   - Enhanced visual feedback

## Implementation Details

### Purchase Flow

1. User taps "Upgrade to Pro" button
2. `BillingManager.launchPurchaseFlow()` is called
3. Google Play billing sheet is displayed
4. User completes purchase
5. Purchase is automatically acknowledged
6. Premium status is updated in PreferencesManager
7. UI updates reactively via `isPremium` flow

### Purchase Validation

- **Local Validation**: Purchases are validated locally using Google Play Billing Library
- **Acknowledgment**: All purchases are automatically acknowledged
- **Server-Side Validation** (Recommended for production): 
  - Implement server-side purchase verification for enhanced security
  - Verify purchase tokens with Google Play Developer API

### Restore Purchases

Users can restore purchases by:
1. Tapping "Restore Purchases" in the Upgrade section
2. `BillingManager.queryPurchases()` queries Google Play for existing purchases
3. Premium status is restored if valid Pro purchase is found

## Google Play Policy Compliance

### ✅ Compliant Practices

1. **No External Payments**: All paid functionality uses Google Play Billing only
2. **Ko-fi Integration**: Ko-fi link is clearly marked as optional donation
   - Located in About section
   - Explicitly labeled as "Optional donation"
   - Does not unlock any features
   - Complies with Google Play policy for voluntary donations

3. **Purchase Acknowledgment**: All purchases are properly acknowledged
4. **Pending Purchases**: Pending purchases are handled gracefully

### ⚠️ Important Notes

- **Testing**: Use Google Play Console's license testing accounts for testing purchases
- **Production**: Implement server-side purchase verification for production apps
- **Refunds**: Handle refunds through Google Play Console (automatic for managed products)

## Testing

### Unit Tests

Unit tests are located in:
- `app/src/test/java/com/aikeyboard/billing/BillingManagerTest.kt`
- `app/src/test/java/com/aikeyboard/billing/PremiumFeaturesManagerTest.kt`

### Testing Purchases

1. **License Testing**:
   - Add test accounts in Google Play Console
   - Use these accounts to test purchases without real charges

2. **Test Products**:
   - Create test products in Google Play Console
   - Use test SKUs for development

3. **Purchase States**:
   - Test successful purchases
   - Test cancelled purchases
   - Test pending purchases
   - Test restore purchases

## Usage Examples

### Checking Premium Status

```kotlin
@Composable
fun MyFeature() {
    val premiumFeaturesManager: PremiumFeaturesManager = hiltViewModel()
    val isPremium by premiumFeaturesManager.isPremium.collectAsStateWithLifecycle()
    
    if (isPremium) {
        PremiumFeatureContent()
    } else {
        UpgradePrompt()
    }
}
```

### Launching Purchase Flow

```kotlin
@Composable
fun UpgradeButton() {
    val viewModel: UpgradeViewModel = hiltViewModel()
    val activity = LocalContext.current as? ComponentActivity
    
    Button(onClick = {
        activity?.let {
            viewModel.launchPurchaseFlow(it, BillingConstants.SKU_PRO_UNLOCK)
        }
    }) {
        Text("Upgrade to Pro")
    }
}
```

## Troubleshooting

### Common Issues

1. **Billing Client Not Connected**
   - Ensure Google Play Services is installed and updated
   - Check internet connection
   - Verify app is signed with correct keystore

2. **Purchase Not Recognized**
   - Check SKU matches Google Play Console configuration
   - Verify purchase acknowledgment
   - Check purchase state in Google Play Console

3. **Restore Purchases Not Working**
   - Ensure user is signed in with correct Google account
   - Verify purchase exists in Google Play Console
   - Check purchase acknowledgment status

## Security Considerations

1. **Purchase Verification**: 
   - Current implementation uses local validation
   - For production, implement server-side verification

2. **Premium Status Storage**:
   - Premium status is stored in encrypted DataStore
   - Combined with real-time billing state for accuracy

3. **Purchase Tokens**:
   - Purchase tokens are handled securely by Google Play Billing Library
   - Never expose purchase tokens in logs or client-side code

## Future Enhancements

Potential improvements:
- Server-side purchase verification
- Subscription model option
- Family sharing support
- Promotional offers
- Grace period for subscription renewals

## Support

For billing-related issues:
- Check Google Play Billing Library documentation
- Review Google Play Console purchase reports
- Test with license testing accounts

## License

This billing implementation is part of AI Keyboard and is licensed under Apache-2.0.

