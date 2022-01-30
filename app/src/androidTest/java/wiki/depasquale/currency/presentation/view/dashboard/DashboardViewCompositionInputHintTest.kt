package wiki.depasquale.currency.presentation.view.dashboard

import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Test
import wiki.depasquale.currency.screen.dashboard.DashboardViewModel
import wiki.depasquale.currency.tooling.ComposeTest

class DashboardViewCompositionInputHintTest : ComposeTest() {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var view: DashboardViewCompositionInputHint

    override fun prepare() {
        viewModel = DashboardViewModel()
        view = DashboardViewCompositionInputHint()
    }

    @Test
    fun hintIsVisible_whenSelectedCurrencyNull() = inCompose {
        view.Compose(model = viewModel)
    } asserts {
        onNodeWithTag("input-hint").assertExists()
            .assertIsDisplayed()
            .assertHasNoClickAction()
    }

}