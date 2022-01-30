package wiki.depasquale.currency.presentation.view

import androidx.compose.runtime.Composable

class ViewCompositionNoop<T> : ViewComposition<T> {
    @Composable
    override fun Compose(model: T) = Unit
}