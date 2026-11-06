package com.dev.movieexplorer.presentation.shop

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned

@Composable
fun CartBadgeIcon(
    count: Int,
    onAnchor: (Offset) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scale = remember { Animatable(1f) }
    LaunchedEffect(count) {
        if (count <= 0) return@LaunchedEffect
        scale.snapTo(1.28f)
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium,
            ),
        )
    }
    Box(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                onAnchor(coordinates.boundsInRoot().center)
            }
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            },
    ) {
        BadgedBox(
            badge = {
                if (count > 0) {
                    Badge(containerColor = MaterialTheme.colorScheme.primary) {
                        Text(if (count > 99) "99+" else count.toString())
                    }
                }
            },
        ) {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = "Cart, $count items",
            )
        }
    }
}
