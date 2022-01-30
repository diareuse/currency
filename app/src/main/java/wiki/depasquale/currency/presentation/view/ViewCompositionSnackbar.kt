package wiki.depasquale.currency.presentation.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsWithImePadding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import wiki.depasquale.currency.screen.style.AnticipateOvershootEasing
import wiki.depasquale.currency.screen.style.ShapeMedium
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class ViewCompositionSnackbar<T>(
    private val source: ViewComposition<T>
) : ViewComposition<T> {

    @Composable
    override fun Compose(model: T) {
        val snackbarController = remember { SnackbarControllerImpl() }
        val snackbarState = snackbarController.asStateFlow().collectAsState()
        val currentState = snackbarState.value
        Box(modifier = Modifier.fillMaxSize()) {
            CompositionLocalProvider(LocalSnackbarController provides snackbarController) {
                source.Compose(model = model)
            }
            AnimatedSnackbar(
                modifier = Modifier
                    .testTag("snackbar-container")
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                model = currentState
            )
            LaunchedEffect(currentState) {
                if (currentState == null) {
                    return@LaunchedEffect
                }
                delay(currentState.duration)
                if (currentState.isVisible)
                    snackbarController.dismiss()
            }
        }
    }

    @Composable
    fun AnimatedSnackbar(modifier: Modifier, model: SnackbarModel?) {
        AnimatedVisibility(
            modifier = modifier,
            visible = model?.isVisible == true,
            enter = slideInVertically(tween(easing = AnticipateOvershootEasing)) { it },
            exit = slideOutVertically(tween(easing = AnticipateOvershootEasing)) { it }
        ) {
            Surface(
                modifier = Modifier
                    .testTag("snackbar-content")
                    .padding(32.dp)
                    .navigationBarsWithImePadding(),
                shape = ShapeMedium,
                color = MaterialTheme.colorScheme.primary,
                shadowElevation = 8.dp
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                    text = model?.text ?: AnnotatedString(""),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

}

val LocalSnackbarController = staticCompositionLocalOf<SnackbarController> {
    throw IllegalStateException("Snackbar Controller was not been provided")
}

@OptIn(ExperimentalTime::class)
data class SnackbarModel(
    val text: AnnotatedString,
    val duration: Duration,
    val isVisible: Boolean
)

@OptIn(ExperimentalTime::class)
interface SnackbarController {

    fun show(model: SnackbarModel)
    fun dismiss()

}

@OptIn(ExperimentalTime::class)
fun SnackbarController.show(
    text: AnnotatedString,
    duration: Duration = 4000.milliseconds
) = show(SnackbarModel(text, duration, true))

@OptIn(ExperimentalTime::class)
fun SnackbarController.show(
    text: String,
    duration: Duration = 4000.milliseconds
) = show(AnnotatedString(text), duration)

@OptIn(ExperimentalTime::class)
class SnackbarControllerImpl : SnackbarController {

    private val currentSnack = MutableStateFlow<SnackbarModel?>(null)

    override fun show(model: SnackbarModel) {
        currentSnack.value = model
    }

    override fun dismiss() {
        currentSnack.value = currentSnack.value?.copy(isVisible = false)
    }

    fun asStateFlow() = currentSnack.asStateFlow()

}