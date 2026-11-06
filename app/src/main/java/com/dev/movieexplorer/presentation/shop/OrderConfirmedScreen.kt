package com.dev.movieexplorer.presentation.shop

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun OrderConfirmedScreen(
    order: PlacedOrder?,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var play by remember { mutableStateOf(false) }
    var showSummary by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        play = true
        delay(520)
        showSummary = true
    }
    val progress by animateFloatAsState(
        targetValue = if (play) 1f else 0f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "check",
    )
    val color = MaterialTheme.colorScheme.primary
    val track = MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Canvas(modifier = Modifier.size(120.dp)) {
            val circle = (progress / 0.45f).coerceIn(0f, 1f)
            val check = ((progress - 0.35f) / 0.65f).coerceIn(0f, 1f)
            drawCircle(color = track, radius = size.minDimension / 2f * circle)
            val path = Path().apply {
                moveTo(size.width * 0.28f, size.height * 0.52f)
                lineTo(size.width * 0.44f, size.height * 0.68f)
                lineTo(size.width * 0.74f, size.height * 0.34f)
            }
            val measure = PathMeasure().apply { setPath(path, false) }
            val segment = Path()
            measure.getSegment(0f, measure.length * check, segment, true)
            drawPath(
                path = segment,
                color = color,
                style = Stroke(
                    width = 8.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                ),
            )
        }
        AnimatedVisibility(
            visible = showSummary,
            enter = fadeIn(tween(400)) + slideInVertically(tween(400)) { it / 3 },
        ) {
            Column(
                modifier = Modifier.padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Order placed",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                )
                if (order != null) {
                    val pieces = order.lines.sumOf { it.quantity }
                    Text(
                        text = "$pieces items  ·  ${formatPrice(order.total)}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    order.lines.forEach { line ->
                        val title = SampleCatalog.find(line.productId)?.title ?: return@forEach
                        Text(
                            text = "${line.quantity} × $title",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                Button(
                    onClick = onContinue,
                    modifier = Modifier.padding(top = 12.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Text("Continue shopping")
                }
            }
        }
    }
}
