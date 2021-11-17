package org.billthefarmer.currency.presentation.view.main

import org.billthefarmer.composition.extra.Alias
import org.billthefarmer.currency.presentation.model.MainViewModel
import org.billthefarmer.currency.presentation.view.ViewComposition

val Main = Alias("main-view-composition")
typealias MainViewComposition = ViewComposition<MainViewModel>