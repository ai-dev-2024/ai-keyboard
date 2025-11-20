package com.aikeyboard.billing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aikeyboard.ui.settings.viewmodel.UpgradeViewModel

/**
 * Composable function to gate premium features.
 * Shows premium content if user has premium, otherwise shows upgrade prompt.
 */
@Composable
fun PremiumFeatureGate(
    premiumContent: @Composable () -> Unit,
    freeContent: @Composable () -> Unit = { PremiumUpgradePrompt() }
) {
    val viewModel: UpgradeViewModel = hiltViewModel()
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle(initialValue = false)
    
    if (isPremium) {
        premiumContent()
    } else {
        freeContent()
    }
}

/**
 * Default upgrade prompt for premium features
 */
@Composable
fun PremiumUpgradePrompt() {
    // This can be customized per feature
    // For now, it's a simple message
    // In practice, you might want to show a card with upgrade button
}

