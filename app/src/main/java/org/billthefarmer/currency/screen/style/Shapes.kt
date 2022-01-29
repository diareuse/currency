package org.billthefarmer.currency.screen.style

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

val ShapeSmall = RoundedCornerShape(8.dp)
val ShapeMedium = RoundedCornerShape(12.dp)
val ShapeLarge = RoundedCornerShape(32.dp)

data class Shapes(
    val small: CornerBasedShape,
    val medium: CornerBasedShape,
    val large: CornerBasedShape
)

object DefaultShapes {

    val shapes = Shapes(
        small = ShapeSmall,
        medium = ShapeMedium,
        large = ShapeLarge
    )

}

val MaterialTheme.shapes get() = DefaultShapes.shapes