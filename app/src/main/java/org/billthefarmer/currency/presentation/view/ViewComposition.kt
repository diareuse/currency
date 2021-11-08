package org.billthefarmer.currency.presentation.view

import androidx.compose.runtime.Composable

interface ViewComposition<Model> {

    @Composable
    fun Compose(model: Model)

}