package com.aikeyboard.ui.settings.sections

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aikeyboard.ui.theme.ThemePresets

@Composable
fun ThemeSelector(
    selectedTheme: String,
    onThemeSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Theme Presets",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(ThemePresets.entries.toList()) { (key, preset) ->
                ThemePreviewTile(
                    themeKey = key,
                    themeName = preset.name,
                    lightColorScheme = preset.lightColorScheme,
                    darkColorScheme = preset.darkColorScheme,
                    isSelected = selectedTheme == key,
                    onClick = { onThemeSelected(key) }
                )
            }
        }
    }
}

@Composable
fun ThemePreviewTile(
    themeKey: String,
    themeName: String,
    lightColorScheme: androidx.compose.material3.ColorScheme,
    darkColorScheme: androidx.compose.material3.ColorScheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "theme_tile_scale"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .scale(scale)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(
                3.dp,
                MaterialTheme.colorScheme.primary
            )
        } else null,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Color preview
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Light theme preview
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .background(
                            lightColorScheme.primary,
                            RoundedCornerShape(6.dp)
                        )
                )
                // Dark theme preview
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .background(
                            darkColorScheme.primary,
                            RoundedCornerShape(6.dp)
                        )
                )
            }
            
            // Secondary color preview
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(20.dp)
                        .background(
                            lightColorScheme.secondary,
                            RoundedCornerShape(4.dp)
                        )
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(20.dp)
                        .background(
                            darkColorScheme.secondary,
                            RoundedCornerShape(4.dp)
                        )
                )
            }
            
            // Theme name
            Text(
                text = themeName,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

@Composable
fun KeycapShapeSelector(
    selectedShape: KeycapShape,
    onShapeSelected: (KeycapShape) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Keycap Shape",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            KeycapShape.values().forEach { shape ->
                KeycapShapeOption(
                    shape = shape,
                    isSelected = selectedShape == shape,
                    onClick = { onShapeSelected(shape) }
                )
            }
        }
    }
}

enum class KeycapShape(val displayName: String, val cornerRadius: Int) {
    ROUNDED("Rounded", 8),
    SQUARE("Square", 0),
    PILL("Pill", 24)
}

@Composable
fun KeycapShapeOption(
    shape: KeycapShape,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "keycap_shape_scale"
    )
    
    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .scale(scale)
                .background(
                    if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    RoundedCornerShape(shape.cornerRadius.dp)
                )
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(shape.cornerRadius.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "A",
                style = MaterialTheme.typography.titleLarge,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
        
        Text(
            text = shape.displayName,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

