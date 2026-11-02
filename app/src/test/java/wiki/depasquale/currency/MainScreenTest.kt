package wiki.depasquale.currency

import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import wiki.depasquale.currency.di.appGraph
import wiki.depasquale.currency.screen.MainActivity

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
class MainScreenTest {

    @get:Rule
    val compose = createEmptyComposeRule()

    @Test
    fun start_screen_shows_value_stored_through_the_app_graph() {
        ApplicationProvider.getApplicationContext<App>().appGraph.latest().value = "4242"

        ActivityScenario.launch(MainActivity::class.java).use {
            compose.onNodeWithText("4242").assertExists()
        }
    }

}
