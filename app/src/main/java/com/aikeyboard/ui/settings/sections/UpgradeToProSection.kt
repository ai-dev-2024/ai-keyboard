package com.aikeyboard.ui.settings.sections

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aikeyboard.billing.BillingConstants
import com.aikeyboard.billing.BillingConnectionState
import com.aikeyboard.billing.PurchaseState
import com.aikeyboard.ui.settings.viewmodel.UpgradeViewModel

@Composable
fun UpgradeToProSection(
    viewModel: UpgradeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle(initialValue = false)
    val purchaseState by viewModel.purchaseState.collectAsStateWithLifecycle()
    val billingConnectionState by viewModel.billingConnectionState.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        viewModel.checkPremiumStatus()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (isPremium) {
            // Premium user view
            PremiumBadge()
            PremiumFeaturesList()
        } else {
            // Upgrade prompt
            UpgradeHeader()
            PremiumFeaturesList()
            UpgradeButton(
                enabled = billingConnectionState is BillingConnectionState.Connected,
                isLoading = purchaseState is PurchaseState.Purchasing || purchaseState is PurchaseState.Loading,
                onUpgradeClick = {
                    if (context is androidx.activity.ComponentActivity) {
                        viewModel.launchPurchaseFlow(context, BillingConstants.SKU_PRO_UNLOCK)
                    }
                }
            )
            
            when (purchaseState) {
                is PurchaseState.Error -> {
                    ErrorMessage((purchaseState as PurchaseState.Error).message)
                }
                is PurchaseState.Cancelled -> {
                    InfoMessage("Purchase cancelled")
                }
                is PurchaseState.Pending -> {
                    InfoMessage("Purchase is pending. Please complete the payment.")
                }
                else -> {}
            }
            
            RestorePurchasesButton(
                enabled = billingConnectionState is BillingConnectionState.Connected,
                onClick = { viewModel.restorePurchases() }
            )
        }
    }
}

@Composable
private fun PremiumBadge() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Star,
                contentDescription = "Premium",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "You have AI Keyboard Pro!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun UpgradeHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Star,
            contentDescription = "Upgrade",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Upgrade to AI Keyboard Pro",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Unlock premium features and support development",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PremiumFeaturesList() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Premium Features",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            PremiumFeatureItem(
                icon = Icons.Default.Palette,
                title = "Premium Themes",
                description = "Access exclusive premium themes and custom theme export/import"
            )
            
            PremiumFeatureItem(
                icon = Icons.Default.ContentCopy,
                title = "Unlimited Clipboard History",
                description = "No limits on clipboard entries and advanced organization"
            )
            
            PremiumFeatureItem(
                icon = Icons.Default.Tune,
                title = "Advanced ASR Model Settings",
                description = "Fine-tune voice recognition models with advanced parameters"
            )
            
            PremiumFeatureItem(
                icon = Icons.Default.Speed,
                title = "Priority Inference Mode",
                description = "Lower latency for faster voice recognition processing"
            )
            
            PremiumFeatureItem(
                icon = Icons.Default.Animation,
                title = "Premium Waveform Animations",
                description = "Beautiful animated waveforms during voice input"
            )
        }
    }
}

@Composable
private fun PremiumFeatureItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun UpgradeButton(
    enabled: Boolean,
    isLoading: Boolean,
    onUpgradeClick: () -> Unit
) {
    Button(
        onClick = onUpgradeClick,
        enabled = enabled && !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Processing...")
        } else {
            Icon(Icons.Default.Star, "Upgrade")
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Upgrade to Pro",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun RestorePurchasesButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(Icons.Default.Restore, "Restore")
        Spacer(modifier = Modifier.width(8.dp))
        Text("Restore Purchases")
    }
}

@Composable
private fun ErrorMessage(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Error,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
private fun InfoMessage(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Info,
                contentDescription = "Info",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

