package wiki.depasquale.currency.presentation.effect

import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NaturalIndicationInstance(
    private val source: AnimatedIndicationSource,
    private val maxMovementPixels: Int = 50,
    private val minimalScale: Float = 0.9f
) : IndicationInstance {

    private val interactions = mutableStateMapOf<PressInteraction.Press, AnimatedIndication>()

    override fun ContentDrawScope.drawIndication() {
        if (interactions.isEmpty()) {
            drawContent()
            return
        }

        interactions.forEach { (_, animation) ->
            withTransform(
                transformBlock = {
                    with(animation) { transform(size) }
                },
                drawBlock = {
                    this@drawIndication.drawContent()
                }
            )
        }
    }

    fun add(interaction: PressInteraction.Press, scope: CoroutineScope) {
        val animation = AnimatedIndicationImpl(source, maxMovementPixels, minimalScale)

        interactions[interaction] = animation

        scope.launch {
            try {
                animation.start()
            } finally {
                interactions.remove(interaction)
            }
        }
    }

    fun remove(interaction: PressInteraction.Press) {
        interactions[interaction]?.finish()
    }

}