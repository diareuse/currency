package org.billthefarmer.currency.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

class ViewCompositionFrame<T>(
    private vararg val views: ViewComposition<T>
) : ViewComposition<T> {

    @Composable
    override fun Compose(model: T) {
        Box(modifier = Modifier.fillMaxSize()) {
            for (view in views)
                view.Compose(model = model)
        }
    }

}