# Premium Features Implementation Guide

This guide explains how to implement premium feature gating in AI Keyboard.

## Quick Start

### 1. Check Premium Status in Composables

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

### 2. Using PremiumFeatureGate

```kotlin
@Composable
fun ClipboardHistory() {
    PremiumFeatureGate(
        premiumContent = {
            // Show unlimited clipboard history
            UnlimitedClipboardList()
        },
        freeContent = {
            // Show limited version or upgrade prompt
            LimitedClipboardList(maxItems = 10)
            UpgradeCard()
        }
    )
}
```

### 3. Check Premium Status in ViewModels

```kotlin
@HiltViewModel
class MyViewModel @Inject constructor(
    private val premiumFeaturesManager: PremiumFeaturesManager
) : ViewModel() {
    
    fun canAccessPremiumFeature(): Boolean {
        return premiumFeaturesManager.hasPremiumAccess()
    }
    
    val isPremium: Flow<Boolean> = premiumFeaturesManager.isPremium
}
```

## Premium Features to Implement

### 1. Premium Themes

**Location**: `AppearanceSettingsSection.kt`

```kotlin
@Composable
fun AppearanceSettingsSection() {
    val isPremium by premiumFeaturesManager.isPremium.collectAsStateWithLifecycle()
    
    // Free themes
    FreeThemeList()
    
    if (isPremium) {
        Divider()
        Text("Premium Themes", style = MaterialTheme.typography.titleMedium)
        PremiumThemeList()
        ThemeExportImport()
    } else {
        PremiumThemeUpgradeCard()
    }
}
```

### 2. Unlimited Clipboard History

**Location**: `ClipboardSettingsSection.kt`

```kotlin
@Composable
fun ClipboardSettingsSection() {
    val isPremium by premiumFeaturesManager.isPremium.collectAsStateWithLifecycle()
    
    val maxItems = if (isPremium) Int.MAX_VALUE else 50
    
    LazyColumn {
        items(clipboardItems.take(maxItems)) { item ->
            ClipboardItem(item)
        }
        
        if (!isPremium && clipboardItems.size > 50) {
            item {
                UpgradeCard(
                    message = "Upgrade to Pro for unlimited clipboard history"
                )
            }
        }
    }
}
```

### 3. Advanced ASR Model Settings

**Location**: `VoiceInputSettingsSection.kt`

```kotlin
@Composable
fun VoiceInputSettingsSection() {
    val isPremium by premiumFeaturesManager.isPremium.collectAsStateWithLifecycle()
    
    BasicModelSettings()
    
    if (isPremium) {
        Divider()
        Text("Advanced Settings (Pro)", style = MaterialTheme.typography.titleMedium)
        AdvancedModelSettings()
    } else {
        PremiumFeatureCard(
            title = "Advanced ASR Settings",
            description = "Fine-tune voice recognition models",
            onUpgrade = { navigateToUpgrade() }
        )
    }
}
```

### 4. Priority Inference Mode

**Location**: Voice input service or settings

```kotlin
class VoiceInputService {
    private val premiumFeaturesManager: PremiumFeaturesManager
    
    fun processAudio(audio: ByteArray) {
        val usePriorityMode = premiumFeaturesManager.hasPremiumAccess()
        
        if (usePriorityMode) {
            // Use optimized, lower-latency processing
            processWithPriorityMode(audio)
        } else {
            // Use standard processing
            processStandard(audio)
        }
    }
}
```

### 5. Premium Waveform Animations

**Location**: Keyboard UI or voice input UI

```kotlin
@Composable
fun VoiceInputWaveform() {
    val isPremium by premiumFeaturesManager.isPremium.collectAsStateWithLifecycle()
    
    if (isPremium) {
        AnimatedWaveform() // Premium animated version
    } else {
        SimpleWaveform() // Basic version
    }
}
```

## Best Practices

1. **Always Check Premium Status Reactively**
   - Use `collectAsStateWithLifecycle()` for Compose
   - Use Flow collection in ViewModels
   - Don't cache premium status

2. **Graceful Degradation**
   - Free users should still have a good experience
   - Premium features should enhance, not block core functionality

3. **Clear Upgrade Prompts**
   - Show what users get with premium
   - Make upgrade path clear and easy

4. **Test Both States**
   - Test with premium enabled
   - Test with premium disabled
   - Test purchase flow
   - Test restore purchases

## Testing Premium Features

### Unit Tests

```kotlin
@Test
fun `premium feature is accessible when user has premium`() {
    `when`(premiumFeaturesManager.hasPremiumAccess()).thenReturn(true)
    
    val result = featureManager.canAccessPremiumFeature()
    
    assertTrue(result)
}
```

### UI Tests

```kotlin
@Test
fun `upgrade prompt shown for non-premium users`() {
    // Set up non-premium state
    composeTestRule.setContent {
        MyFeature()
    }
    
    // Verify upgrade prompt is shown
    composeTestRule.onNodeWithText("Upgrade to Pro").assertIsDisplayed()
}
```

## Migration Checklist

When adding premium gating to existing features:

- [ ] Identify the premium feature
- [ ] Add premium check using `PremiumFeaturesManager`
- [ ] Implement free version (if needed)
- [ ] Add upgrade prompt/card
- [ ] Update UI to show premium badge (if applicable)
- [ ] Test both premium and free states
- [ ] Update documentation
- [ ] Add to premium features list in Upgrade section

## Common Patterns

### Pattern 1: Feature Toggle

```kotlin
val isPremium = premiumFeaturesManager.isPremium.collectAsStateWithLifecycle()
val featureEnabled = if (isPremium.value) true else false
```

### Pattern 2: Limit-Based

```kotlin
val maxItems = if (isPremium.value) Int.MAX_VALUE else FREE_LIMIT
```

### Pattern 3: UI Variant

```kotlin
if (isPremium.value) {
    PremiumUI()
} else {
    FreeUI()
}
```

### Pattern 4: Service-Level Check

```kotlin
class MyService {
    fun process() {
        if (premiumFeaturesManager.hasPremiumAccess()) {
            premiumProcess()
        } else {
            standardProcess()
        }
    }
}
```

## Support

For questions about premium feature implementation:
- Check `BILLING_DOCUMENTATION.md` for billing details
- Review existing implementations in settings sections
- Test with license testing accounts in Google Play Console

