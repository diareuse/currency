package org.billthefarmer.currency.presentation.view.detail

import org.billthefarmer.composition.extra.Alias
import org.billthefarmer.currency.presentation.view.ViewComposition
import org.billthefarmer.currency.ui.detail.DetailViewModel

val Detail = Alias("detail-view-composition")
typealias DetailViewComposition = ViewComposition<DetailViewModel>