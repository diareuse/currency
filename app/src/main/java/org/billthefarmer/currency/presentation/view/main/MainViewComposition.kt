package org.billthefarmer.currency.presentation.view.main

import org.billthefarmer.composition.extra.Alias
import org.billthefarmer.currency.presentation.view.ViewComposition
import org.billthefarmer.currency.screen.MainViewModel

val Main = Alias("main-view-composition")
typealias MainViewComposition = ViewComposition<MainViewModel>