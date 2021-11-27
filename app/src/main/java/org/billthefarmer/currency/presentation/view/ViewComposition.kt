package org.billthefarmer.currency.presentation.view

import androidx.compose.runtime.Composable

fun interface ViewComposition<Model> {

    @Composable
    fun Compose(model: Model)

}