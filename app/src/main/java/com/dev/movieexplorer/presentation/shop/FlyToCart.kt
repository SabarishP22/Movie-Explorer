package com.dev.movieexplorer.presentation.shop

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.first
import kotlin.math.roundToInt
import kotlin.math.sin

class FlyToCartState {
    var target by mutableStateOf(Offset.Unspecified)
    val flights = mutableStateListOf<Flight>()
    private var nextId = 0L

    fun launch(start: Offset, product: Product) {
        if (start == Offset.Unspecified || start == Offset.Zero) return
        flights += Flight(
            id = nextId++,
            start = start,
            product = product,
        )
    }

    fun finish(id: Long) {
        flights.removeAll { it.id == id }
    }
}

data class Flight(
    val id: Long,
    val start: Offset,
    val product: Product,
)

@Composable
fun FlyToCartOverlay(
    state: FlyToCartState,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        state.flights.forEach { flight ->
            FlyingImage(
                flight = flight,
                state = state,
            )
        }
    }
}

@Composable
private fun FlyingImage(
    flight: Flight,
    state: FlyToCartState,
) {
    val progress = remember(flight.id) { Animatable(0f) }
    var destination by remember(flight.id) { mutableStateOf<Offset?>(null) }
    LaunchedEffect(flight.id) {
        destination = snapshotFlow { state.target }.first { it != Offset.Unspecified }
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 680, easing = FastOutSlowInEasing),
        )
        state.finish(flight.id)
    }
    val landing = destination ?: return
    val t = progress.value
    val x = lerp(flight.start.x, landing.x, t)
    val y = lerp(flight.start.y, landing.y, t) - sin(t * Math.PI).toFloat() * 110f
    val scale = lerp(1f, 0.28f, t)
    val shape = RoundedCornerShape(16.dp)
    ProductImage(
        product = flight.product,
        contentDescription = null,
        modifier = Modifier
            .offset {
                val edge = 72.dp.toPx()
                IntOffset(
                    (x - edge / 2f).roundToInt(),
                    (y - edge / 2f).roundToInt(),
                )
            }
            .size(72.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                alpha = 1f - (0.2f * t)
            }
            .shadow(10.dp, shape)
            .clip(shape),
    )
}

private fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start + (stop - start) * fraction
}
