package wiki.depasquale.currency.presentation.view.main

import wiki.depasquale.composition.extra.Alias
import wiki.depasquale.currency.presentation.view.ViewComposition
import wiki.depasquale.currency.screen.MainViewModel

val Main = Alias("main-view-composition")
typealias MainViewComposition = ViewComposition<MainViewModel>