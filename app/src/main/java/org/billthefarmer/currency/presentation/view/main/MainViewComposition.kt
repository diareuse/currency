package org.billthefarmer.currency.presentation.view.main

import org.billthefarmer.currency.composition.core.Alias
import org.billthefarmer.currency.presentation.model.MainViewModel
import org.billthefarmer.currency.presentation.view.ViewComposition

val Main = Alias("main-view-composition")
typealias MainViewComposition = ViewComposition<MainViewModel>