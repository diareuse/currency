package org.billthefarmer.currency.presentation.view.main

import androidx.compose.foundation.LocalIndication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import org.billthefarmer.currency.presentation.effect.NaturalIndication
import org.billthefarmer.currency.screen.MainViewModel
import org.billthefarmer.currency.screen.style.CurrencyTheme

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