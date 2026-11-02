package wiki.depasquale.currency.screen

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
import wiki.depasquale.currency.screen.style.CurrencyTheme

class MainActivity : AppCompatActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val viewModels = appGraph.viewModelEntryPointFactory
        setContent {
            CompositionLocalProvider(LocalViewModelEntryPointFactory provides viewModels) {
                CurrencyTheme {
                    Navigation(calculateWindowSizeClass(activity = this))
                }
            }
        }
    }

}