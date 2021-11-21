package org.billthefarmer.currency.presentation.view.main

import androidx.compose.runtime.Composable
import org.billthefarmer.currency.ui.MainViewModel
import org.billthefarmer.currency.ui.style.CurrencyTheme

class MainViewCompositionTheme(
    private val source: MainViewComposition
) : MainViewComposition {

    @Composable
    override fun Compose(model: MainViewModel) {
        CurrencyTheme {
            source.Compose(model = model)
        }
    }

}