package wiki.depasquale.currency.platform.process

import android.content.ClipboardManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalConfiguration
import cursola.rate.view.ConvertTextViewModel
import spike.compose.LocalViewModelEntryPointFactory
import spike.lifecycle.viewmodel.compose.spikeViewModel
import wiki.depasquale.currency.R
import wiki.depasquale.currency.di.appGraph
import wiki.depasquale.currency.di.viewModelEntryPointFactory
import wiki.depasquale.currency.screen.MainActivity
import wiki.depasquale.currency.screen.style.CurrencyTheme

class ConvertTextActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.action != Intent.ACTION_PROCESS_TEXT) {
            finish()
            return
        }
        guardAgainstOverlays()
        enableEdgeToEdge()
        val text = selectedText() ?: run {
            finish()
            return
        }
        showSheet(text)
    }

    private fun showSheet(text: CharSequence) {
        val viewModels = appGraph.viewModelEntryPointFactory
        setContent {
            CompositionLocalProvider(LocalViewModelEntryPointFactory provides viewModels) {
                CurrencyTheme {
                    val viewModel = spikeViewModel<ConvertTextViewModel>()
                    val locale = LocalConfiguration.current.locales[0]
                    LaunchedEffect(text, locale) { viewModel.convert(text, locale) }
                    val state by viewModel.state.collectAsState()
                    ConvertTextScreen(
                        state = state,
                        onSelect = { select(it.value) },
                        onOpenApp = ::openApp,
                        onDismiss = ::finish
                    )
                }
            }
        }
    }

    private fun selectedText(): CharSequence? = try {
        intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT) ?: ""
    } catch (_: RuntimeException) {
        null
    }

    private fun guardAgainstOverlays() {
        window.decorView.filterTouchesWhenObscured = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            window.setHideOverlayWindows(true)
        }
    }

    private fun select(value: String) {
        target().accept(value)
        finish()
    }

    private fun openApp() {
        startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        finish()
    }

    private fun target(): ConvertedValueTarget {
        val readOnly = intent.getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false)
        if (!readOnly) return ConvertedValueReplacesSelection(this)
        val clipboard = ConvertedValueToClipboard(
            getSystemService(ClipboardManager::class.java),
            getString(R.string.convert_text_label)
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) return clipboard
        return ConvertedValueConfirmedByToast(clipboard, applicationContext, getString(R.string.convert_text_copied))
    }

}
