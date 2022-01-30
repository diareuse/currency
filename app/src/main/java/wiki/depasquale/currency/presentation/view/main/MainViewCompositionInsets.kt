package wiki.depasquale.currency.presentation.view.main

import androidx.compose.runtime.Composable
import com.google.accompanist.insets.ProvideWindowInsets
import wiki.depasquale.currency.screen.MainViewModel

class MainViewCompositionInsets(
    private val source: MainViewComposition
) : MainViewComposition {

    @Composable
    override fun Compose(model: MainViewModel) {
        ProvideWindowInsets(consumeWindowInsets = true, windowInsetsAnimationsEnabled = true) {
            source.Compose(model = model)
        }
    }

}