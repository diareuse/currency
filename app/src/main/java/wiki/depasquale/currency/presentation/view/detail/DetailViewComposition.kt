package wiki.depasquale.currency.presentation.view.detail

import wiki.depasquale.composition.extra.Alias
import wiki.depasquale.currency.presentation.view.ViewComposition
import wiki.depasquale.currency.screen.detail.DetailViewModel

val Detail = Alias("detail-view-composition")
typealias DetailViewComposition = ViewComposition<DetailViewModel>