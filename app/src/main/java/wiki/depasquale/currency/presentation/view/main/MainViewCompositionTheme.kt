package wiki.depasquale.currency.presentation.view.main

import androidx.compose.foundation.LocalIndication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import wiki.depasquale.currency.presentation.effect.NaturalIndication
import wiki.depasquale.currency.screen.MainViewModel
import wiki.depasquale.currency.screen.style.CurrencyTheme

class MainViewCompositionTheme(
    private val source: MainViewComposition
) : MainViewComposition {

    @Composable
    override fun Compose(model: MainViewModel) {
        CurrencyTheme {
            CompositionLocalProvider(LocalIndication provides NaturalIndication()) {
                source.Compose(model = model)
            }
        }
    }

}