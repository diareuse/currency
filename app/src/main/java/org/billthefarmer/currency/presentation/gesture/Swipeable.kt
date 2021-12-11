package org.billthefarmer.currency.presentation.gesture

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.abs
import kotlin.math.max

fun Modifier.swipeable(
    getActionsSize: () -> Int,
    getOffset: () -> Float,
    onOffsetChanged: (Float) -> Unit
) = pointerInput(Unit) {
    detectHorizontalDragGestures(
        onDragEnd = {
            val newOffset = when {
                abs(getOffset()) > abs(getActionsSize() / 2) -> -getActionsSize().toFloat()
                else -> 0f
            }
            onOffsetChanged(newOffset)
        }
    ) { change, dragAmount ->
        val newOffset = max(-getActionsSize().toFloat(), getOffset() + dragAmount)
            .coerceAtMost(0f)
        onOffsetChanged(newOffset)
        change.consumePositionChange()
    }
}