package com.dev.movieexplorer.presentation.shop

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun TrackingScreen(
    order: ShopOrder,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val target = order.stage.ordinal / (TrackingStage.entries.lastIndex).toFloat()
    val traveled by animateFloatAsState(
        targetValue = target.coerceIn(0.08f, 1f),
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "traveled",
    )
    val pulse by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 1f,
        targetValue = 1.45f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "pulseScale",
    )
    val routeColor = MaterialTheme.colorScheme.outline
    val ink = MaterialTheme.colorScheme.primary

    Column(modifier = modifier.fillMaxSize()) {
        ShopBackBar(title = order.id, onBack = onBack)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(text = order.eta, style = MaterialTheme.typography.headlineMedium)
            Text(
                text = "${order.carrier}  ·  ${order.placedOn}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
            ) {
                Canvas(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    val path = Path().apply {
                        moveTo(8.dp.toPx(), size.height * 0.72f)
                        quadraticTo(
                            size.width * 0.35f,
                            size.height * 0.05f,
                            size.width * 0.62f,
                            size.height * 0.48f,
                        )
                        quadraticTo(
                            size.width * 0.82f,
                            size.height * 0.78f,
                            size.width - 8.dp.toPx(),
                            size.height * 0.32f,
                        )
                    }
                    drawPath(
                        path = path,
                        color = routeColor,
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round),
                    )
                    val measure = PathMeasure().apply { setPath(path, false) }
                    val walked = Path()
                    measure.getSegment(0f, measure.length * traveled, walked, true)
                    drawPath(
                        path = walked,
                        color = ink,
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round),
                    )
                    val point = measure.getPosition(measure.length * traveled)
                    drawCircle(color = ink.copy(alpha = 0.25f), radius = 14.dp.toPx() * pulse, center = point)
                    drawCircle(color = ink, radius = 7.dp.toPx(), center = point)
                }
            }
            Text(text = order.address, style = MaterialTheme.typography.bodyLarge)
            TrackingStage.entries.forEachIndexed { index, stage ->
                val reached = index <= order.stage.ordinal
                val event = order.events.getOrNull(index)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(if (index == order.stage.ordinal) 16.dp else 12.dp)
                                .clip(CircleShape)
                                .then(Modifier)
                                .padding(0.dp),
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawCircle(color = if (reached) ink else routeColor)
                            }
                        }
                        if (index < TrackingStage.entries.lastIndex) {
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .width(2.dp)
                                    .height(28.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                            ) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    drawRect(color = if (index < order.stage.ordinal) ink else routeColor)
                                }
                            }
                        }
                    }
                    Column(modifier = Modifier.padding(bottom = 8.dp)) {
                        Text(
                            text = stage.label,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (reached) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                        )
                        if (event != null) {
                            Text(
                                text = "${event.detail}  ·  ${event.time}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
            order.lines.forEach { line ->
                val product = SampleCatalog.find(line.productId) ?: return@forEach
                Text(
                    text = "${line.quantity} × ${product.title}",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            Text(
                text = formatPrice(order.total),
                modifier = Modifier.padding(bottom = 28.dp),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
