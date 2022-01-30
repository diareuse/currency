package wiki.depasquale.currency.presentation.view.selection

import wiki.depasquale.composition.extra.Alias
import wiki.depasquale.currency.presentation.view.ViewComposition
import wiki.depasquale.currency.screen.selection.SelectionViewModel

val Selection = Alias("selection-view-composition")
typealias SelectionViewComposition = ViewComposition<SelectionViewModel>