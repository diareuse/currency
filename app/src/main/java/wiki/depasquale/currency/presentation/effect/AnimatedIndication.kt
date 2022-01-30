package wiki.depasquale.currency.presentation.effect

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawTransform

interface AnimatedIndication {

    suspend fun start()
    fun finish()

    fun DrawTransform.transform(size: Size)

}