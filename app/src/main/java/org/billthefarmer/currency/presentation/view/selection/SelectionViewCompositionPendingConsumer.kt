package org.billthefarmer.currency.presentation.view.selection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.billthefarmer.currency.domain.model.ExchangeRatePreference
import org.billthefarmer.currency.domain.preference.ExchangeRatePreferenceReader
import org.billthefarmer.currency.domain.preference.ExchangeRatePreferenceWriter
import org.billthefarmer.currency.presentation.model.CurrencyModel
import org.billthefarmer.currency.presentation.view.LocalSnackbarController
import org.billthefarmer.currency.presentation.view.show
import org.billthefarmer.currency.screen.selection.SelectionViewModel

class SelectionViewCompositionPendingConsumer(
    private val source: SelectionViewComposition,
    private val reader: ExchangeRatePreferenceReader,
    private val writer: ExchangeRatePreferenceWriter
) : SelectionViewComposition {

    @Composable
    override fun Compose(model: SelectionViewModel) {
        source.Compose(model = model)
        val pending by model.pendingCurrencies.collectAsState()
        if (pending.isEmpty()) {
            return
        }
        val snackbar = LocalSnackbarController.current
        LaunchedEffect(pending) {
            val preference = readPreference().addAll(pending)
            writePreference(preference)
            model.removePending(pending)
            snackbar.show("Added ${pending.first().rate.currency.displayName}")
        }
    }

    private suspend fun readPreference() = withContext(Dispatchers.IO) {
        reader.read()
    }

    private suspend fun writePreference(
        preference: ExchangeRatePreference
    ) = withContext(Dispatchers.IO) {
        writer.write(preference)
    }

    private suspend fun ExchangeRatePreference.addAll(
        pending: List<CurrencyModel>
    ) = withContext(Dispatchers.IO) {
        val currencies = selectedCurrencies.toHashSet()
        currencies += pending.map { it.rate.currency }.toSet()
        copy(selectedCurrencies = currencies)
    }

}
