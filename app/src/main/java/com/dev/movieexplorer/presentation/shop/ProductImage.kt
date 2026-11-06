package com.dev.movieexplorer.presentation.shop

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun ProductImage(
    product: Product,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    Box(modifier = modifier.background(product.fallback)) {
        AsyncImage(
            model = product.imageUrl,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.SharedProductImage(
    product: Product,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    contentDescription: String? = product.title,
) {
    ProductImage(
        product = product,
        contentDescription = contentDescription,
        modifier = modifier.sharedElement(
            sharedContentState = rememberSharedContentState(key = sharedImageKey(product.id)),
            animatedVisibilityScope = animatedVisibilityScope,
        ),
    )
}
