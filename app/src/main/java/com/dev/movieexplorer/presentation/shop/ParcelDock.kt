package com.dev.movieexplorer.presentation.shop

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.material3.LocalContentColor

private data class DockSlot(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
)

@Composable
fun ParcelDock(
    current: String?,
    itemCount: Int,
    unreadCount: Int,
    onCartAnchor: (Offset) -> Unit,
    onHome: () -> Unit,
    onBrowse: () -> Unit,
    onCart: () -> Unit,
    onOrders: () -> Unit,
    onYou: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val slots = listOf(
        DockSlot("home", "Home", Icons.Filled.Home, onHome),
        DockSlot("categories", "Browse", Icons.Filled.GridView, onBrowse),
        DockSlot("cart", "Cart", Icons.Filled.Home, onCart),
        DockSlot("orders", "Orders", Icons.Filled.LocalShipping, onOrders),
        DockSlot("profile", "You", Icons.Filled.Person, onYou),
    )
    val selectedIndex = slots.indexOfFirst { it.route == current }.let { if (it < 0) 0 else it }
    val bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val cartLift by animateFloatAsState(
        targetValue = if (current == "cart") 1.08f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "cartLift",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = bottom + 8.dp)
            .height(92.dp),
    ) {
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(72.dp),
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 18.dp,
            tonalElevation = 3.dp,
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val slot = maxWidth / slots.size
                val pillWidth = slot * 0.78f
                val pillX by animateDpAsState(
                    targetValue = slot * selectedIndex + (slot - pillWidth) / 2,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMediumLow,
                    ),
                    label = "dockPill",
                )
                Box(
                    modifier = Modifier
                        .offset(x = pillX, y = 11.dp)
                        .size(width = pillWidth, height = 50.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)),
                )
                Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                    slots.forEach { slotItem ->
                        if (slotItem.route == "cart") {
                            Spacer(modifier = Modifier.weight(1f))
                        } else {
                            DockItem(
                                slot = slotItem,
                                selected = current == slotItem.route,
                                showDot = slotItem.route == "profile" && unreadCount > 0,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 2.dp)
                .graphicsLayer {
                    scaleX = cartLift
                    scaleY = cartLift
                }
                .size(62.dp)
                .shadow(10.dp, CircleShape)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable(role = Role.Button, onClick = onCart),
            contentAlignment = Alignment.Center,
        ) {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onPrimary) {
                CartBadgeIcon(count = itemCount, onAnchor = onCartAnchor)
            }
        }
    }
}

@Composable
private fun DockItem(
    slot: DockSlot,
    selected: Boolean,
    showDot: Boolean,
    modifier: Modifier = Modifier,
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.12f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "dockIcon",
    )
    val tint by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "dockTint",
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .clickable(role = Role.Tab, onClick = slot.onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
    ) {
        Box {
            Icon(
                imageVector = slot.icon,
                contentDescription = slot.label,
                tint = tint,
                modifier = Modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            )
            if (showDot) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                )
            }
        }
        Text(
            text = slot.label,
            style = MaterialTheme.typography.labelSmall,
            color = tint,
        )
    }
}
