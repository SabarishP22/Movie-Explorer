package com.dev.movieexplorer.presentation.shop

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ProductScreen(
    product: Product,
    cartCount: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    saved: Boolean,
    onToggleSaved: () -> Unit,
    onBack: () -> Unit,
    onOpenCart: () -> Unit,
    onCartAnchor: (Offset) -> Unit,
    onAddToCart: (colorName: String, origin: Offset) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selected by rememberSaveable { mutableStateOf(product.colors.first().name) }
    var buttonCenter by remember { mutableStateOf(Offset.Unspecified) }
    val heartScale by animateFloatAsState(
        targetValue = if (saved) 1.15f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "heart",
    )
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = topInset),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = product.title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            IconButton(onClick = onToggleSaved) {
                Icon(
                    imageVector = if (saved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (saved) "Remove from wishlist" else "Save to wishlist",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.graphicsLayer {
                        scaleX = heartScale
                        scaleY = heartScale
                    },
                )
            }
            IconButton(onClick = onOpenCart) {
                CartBadgeIcon(count = cartCount, onAnchor = onCartAnchor)
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            with(sharedTransitionScope) {
                SharedProductImage(
                    product = product,
                    animatedVisibilityScope = animatedVisibilityScope,
                    contentDescription = product.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(28.dp)),
                )
            }
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(text = product.category, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = formatPrice(product.price), style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                    Text(
                        text = "${product.rating}  ·  ${formatCount(product.reviewCount)} ratings",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Text(text = product.blurb, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = "Free delivery by Sunday with Parcel Plus",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(text = "Color", style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    product.colors.forEach { swatch ->
                        ColorChoice(
                            swatch = swatch,
                            selected = swatch.name == selected,
                            onClick = { selected = swatch.name },
                        )
                    }
                }
                Text(text = "Reviews", style = MaterialTheme.typography.titleMedium)
                sampleReviews(product).forEach { (name, quote) ->
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(text = name, style = MaterialTheme.typography.labelLarge)
                        Text(
                            text = quote,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
        Button(
            onClick = { onAddToCart(selected, buttonCenter) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = bottomInset + 12.dp)
                .onGloballyPositioned { coordinates ->
                    buttonCenter = coordinates.boundsInRoot().center
                },
            shape = RoundedCornerShape(16.dp),
        ) {
            Text("Add to cart")
        }
    }
}

private fun sampleReviews(product: Product): List<Pair<String, String>> {
    return listOf(
        "Maya R." to "Packed carefully. ${product.title} looks like the photo.",
        "Rohit K." to "Arrived a day early. Would order again.",
        "Leah P." to "Color is true and the finish feels solid.",
    )
}

@Composable
private fun ColorChoice(
    swatch: Swatch,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val ring by animateDpAsState(targetValue = if (selected) 3.dp else 0.dp, label = "swatchRing")
    val size by animateDpAsState(targetValue = if (selected) 36.dp else 28.dp, label = "swatchSize")
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(
            modifier = Modifier
                .size(size + 8.dp)
                .clip(CircleShape)
                .border(ring, MaterialTheme.colorScheme.primary, CircleShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(swatch.color)
                    .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape),
            )
        }
        Text(text = swatch.name, style = MaterialTheme.typography.bodyMedium)
    }
}
