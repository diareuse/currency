package wiki.depasquale.currency.screen

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import spike.compose.LocalViewModelEntryPointFactory
import wiki.depasquale.currency.di.appGraph
import wiki.depasquale.currency.di.viewModelEntryPointFactory
import wiki.depasquale.currency.platform.link.LaunchDestination
import wiki.depasquale.currency.screen.style.CurrencyTheme

class MainActivity : AppCompatActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val viewModels = appGraph.viewModelEntryPointFactory
        val replayed = savedInstanceState != null || intent.flags and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY != 0
        val destination = when {
            replayed -> LaunchDestination.Start
            else -> appGraph.launchDestinations.parse(intent.dataString)
        }
        setContent {
            CompositionLocalProvider(LocalViewModelEntryPointFactory provides viewModels) {
                CurrencyTheme {
                    Navigation(calculateWindowSizeClass(activity = this), destination)
                }
            }
        }
    }

}