package org.billthefarmer.currency.presentation.gesture

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import kotlin.math.abs
import kotlin.math.absoluteValue

fun Modifier.dismissVertically(
    maxOffset: Dp = DismissVerticallyDefaults.maxOffset,
    dismissDirection: DismissDirectionVertical = DismissVerticallyDefaults.dismissDirection,
    dampingRatio: Float = DismissVerticallyDefaults.dampingRatio,
    onDismiss: () -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "dismiss-vertically"
        properties["maxOffset"] = maxOffset
        properties["dismissDirection"] = dismissDirection
        properties["dampingRatio"] = dampingRatio
    }
) {
    val maxOffsetInt = with(LocalDensity.current) { maxOffset.roundToPx().absoluteValue }
    var requiresImmediateFeedback by remember { mutableStateOf(false) }
    var offsetDrag by remember { mutableStateOf(0) }
    val offsetAnimated by animateIntAsState(
        targetValue = offsetDrag,
        animationSpec = tween(200)
    )
    offset {
        IntOffset(x = 0, y = if (requiresImmediateFeedback) offsetDrag else offsetAnimated)
    } then pointerInput(Unit) {
        detectVerticalDragGestures(
            onDragStart = { requiresImmediateFeedback = true },
            onDragCancel = { requiresImmediateFeedback = false },
            onDragEnd = {
                requiresImmediateFeedback = false
                if (abs(offsetDrag) > maxOffsetInt * .5f) {
                    onDismiss()
                }
                offsetDrag = 0
            },
            onVerticalDrag = { _, amount ->
                val newOffset = offsetDrag + (amount * dampingRatio).toInt()
                offsetDrag = when (dismissDirection) {
                    DismissDirectionVertical.Up -> newOffset.coerceIn(-maxOffsetInt, 0)
                    DismissDirectionVertical.Down -> newOffset.coerceIn(0, maxOffsetInt)
                    DismissDirectionVertical.Both -> newOffset.coerceIn(-maxOffsetInt, maxOffsetInt)
                }
            }
        )
    }
}