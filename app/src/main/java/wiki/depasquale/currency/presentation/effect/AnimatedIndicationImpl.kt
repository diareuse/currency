package wiki.depasquale.currency.presentation.effect

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawTransform
import androidx.compose.ui.graphics.drawscope.scale
import kotlinx.coroutines.CompletableDeferred
import kotlin.math.max

class AnimatedIndicationImpl(
    private val source: AnimatedIndicationSource,
    private val maxMovementPixels: Int,
    private val minimalScale: Float
) : AnimatedIndication {

    private val signalFinish = CompletableDeferred<Unit>(null)

    override suspend fun start() {
        source.animatePress()
        signalFinish.await()
        source.animateRelease()
    }

    override fun finish() {
        signalFinish.complete(Unit)
    }

    override fun DrawTransform.transform(size: Size) {
        val sizeMax = size.maxDimension
        val sizePreferred = sizeMax - maxMovementPixels
        val scalePreferred = sizePreferred / sizeMax
        val scaleAdjusted = max(scalePreferred, minimalScale)
        val scaleAnimatable = 1f - scaleAdjusted
        val scale = source.progress * scaleAnimatable + scaleAdjusted
        scale(scale)
    }

}