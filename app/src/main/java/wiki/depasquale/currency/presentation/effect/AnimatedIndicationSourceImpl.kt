package wiki.depasquale.currency.presentation.effect

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class AnimatedIndicationSourceImpl : AnimatedIndicationSource {

    private val animatedProgress = Animatable(1f)

    override val progress: Float
        get() = animatedProgress.value

    override suspend fun animatePress() {
        coroutineScope {
            launch {
                animatedProgress.animateTo(
                    0f,
                    animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing)
                )
            }
        }
    }

    override suspend fun animateRelease() {
        coroutineScope {
            launch {
                animatedProgress.animateTo(
                    1f,
                    animationSpec = tween(durationMillis = 100, easing = LinearOutSlowInEasing)
                )
            }
        }
    }
}