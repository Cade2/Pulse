package com.cade2.pulse.ui.swipe

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cade2.pulse.domain.model.EmotionCard
import com.cade2.pulse.ui.theme.Surface
import com.cade2.pulse.ui.theme.SwipeLeft
import com.cade2.pulse.ui.theme.SwipeRight
import com.cade2.pulse.util.Constants
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun SwipeCardView(
    card: EmotionCard,
    onSwipeRight: () -> Unit,
    onSwipeLeft: () -> Unit,
    modifier: Modifier = Modifier,
    isTop: Boolean = false,
    backgroundScale: Float = 1f
) {
    val coroutineScope = rememberCoroutineScope()
    val haptics = LocalHapticFeedback.current
    val density = LocalDensity.current

    val offsetX = remember { Animatable(0f) }
    val flipRotationY = remember { Animatable(0f) }
    var isFlipped by remember { mutableStateOf(false) }

    val swipeThresholdPx = with(density) { Constants.SWIPE_THRESHOLD_DP.dp.toPx() }

    val rotation = (offsetX.value / swipeThresholdPx * Constants.MAX_ROTATION_DEGREES)
        .coerceIn(-Constants.MAX_ROTATION_DEGREES, Constants.MAX_ROTATION_DEGREES)
    val overlayAlpha = (abs(offsetX.value) / swipeThresholdPx).coerceIn(0f, 1f)
    val isSwipingRight = offsetX.value > 0

    Box(
        modifier = modifier
            .scale(backgroundScale)
            .shadow(
                elevation = if (isTop) 16.dp else 8.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(Surface)
            .graphicsLayer {
                translationX = if (isTop) offsetX.value else 0f
                rotationZ = if (isTop) rotation else 0f
                rotationY = flipRotationY.value
                cameraDistance = 12f * density.density
            }
            .pointerInput(isTop) {
                if (!isTop) return@pointerInput
                detectDragGestures(
                    onDragEnd = {
                        coroutineScope.launch {
                            if (abs(offsetX.value) > swipeThresholdPx) {
                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                val direction = if (offsetX.value > 0) 1f else -1f
                                offsetX.animateTo(
                                    direction * 2000f,
                                    animationSpec = tween(durationMillis = 300)
                                )
                                if (direction > 0) onSwipeRight() else onSwipeLeft()
                            } else {
                                offsetX.animateTo(
                                    0f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                            }
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        coroutineScope.launch {
                            offsetX.snapTo(offsetX.value + dragAmount.x)
                        }
                    }
                )
            }
            .clickable(enabled = isTop) {
                coroutineScope.launch {
                    isFlipped = !isFlipped
                    flipRotationY.animateTo(
                        targetValue = if (isFlipped) 180f else 0f,
                        animationSpec = tween(durationMillis = 400)
                    )
                }
            }
    ) {
        // Front face
        if (flipRotationY.value <= 90f) {
            CardFrontContent(card = card)
        } else {
            // Back face — apply counter-rotation so text reads correctly
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationY = 180f }
            ) {
                CardBackContent(card = card)
            }
        }

        // Swipe direction overlay
        if (isTop && overlayAlpha > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = if (isSwipingRight) {
                            SwipeRight.copy(alpha = overlayAlpha * 0.4f)
                        } else {
                            SwipeLeft.copy(alpha = overlayAlpha * 0.3f)
                        }
                    )
            )
        }
    }
}

@Composable
private fun CardFrontContent(card: EmotionCard) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = card.emoji,
            fontSize = 56.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = card.name,
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = card.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Tap to read more",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun CardBackContent(card: EmotionCard) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        Text(
            text = card.emoji,
            fontSize = 36.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = card.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = card.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            lineHeight = 26.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Tap to flip back",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}
