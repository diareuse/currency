package wiki.depasquale.currency.presentation.effect

import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.collect

class NaturalIndication(
    private val source: AnimatedIndicationSource = AnimatedIndicationSourceImpl(),
    private val maxMovementPixels: Int = 50,
    private val minimalScale: Float = 0.9f
) : Indication {
    @Composable
    override fun rememberUpdatedInstance(interactionSource: InteractionSource): IndicationInstance {
        val instance = remember(interactionSource, this) {
            NaturalIndicationInstance(source, maxMovementPixels, minimalScale)
        }

        LaunchedEffect(interactionSource, instance) {
            interactionSource.interactions.collect {
                when (it) {
                    is PressInteraction.Press -> instance.add(it, this)
                    is PressInteraction.Release -> instance.remove(it.press)
                    is PressInteraction.Cancel -> instance.remove(it.press)
                }
            }
        }

        return instance
    }
}