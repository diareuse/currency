package wiki.depasquale.currency.presentation.view

import androidx.compose.runtime.Composable

@Suppress("ObjectLiteralToLambda")
fun <T> ViewComposition(body: @Composable (T) -> Unit): ViewComposition<T> {
    return object : ViewComposition<T> {
        @Composable
        override fun Compose(model: T) {
            body(model)
        }
    }
}