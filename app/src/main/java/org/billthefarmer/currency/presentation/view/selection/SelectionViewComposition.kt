package org.billthefarmer.currency.presentation.view.selection

import org.billthefarmer.composition.extra.Alias
import org.billthefarmer.currency.presentation.view.ViewComposition
import org.billthefarmer.currency.screen.selection.SelectionViewModel

val Selection = Alias("selection-view-composition")
typealias SelectionViewComposition = ViewComposition<SelectionViewModel>