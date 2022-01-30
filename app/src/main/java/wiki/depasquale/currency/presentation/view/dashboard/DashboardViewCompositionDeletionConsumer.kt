package wiki.depasquale.currency.presentation.view.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import wiki.depasquale.currency.domain.model.ExchangeRatePreference
import wiki.depasquale.currency.domain.preference.ExchangeRatePreferenceReader
import wiki.depasquale.currency.domain.preference.ExchangeRatePreferenceWriter
import wiki.depasquale.currency.presentation.model.CurrencyModel
import wiki.depasquale.currency.presentation.view.LocalSnackbarController
import wiki.depasquale.currency.presentation.view.show
import wiki.depasquale.currency.screen.dashboard.DashboardViewModel

class DashboardViewCompositionDeletionConsumer(
    private val source: DashboardViewComposition,
    private val reader: ExchangeRatePreferenceReader,
    private val writer: ExchangeRatePreferenceWriter
) : DashboardViewComposition {

    @Composable
    override fun Compose(model: DashboardViewModel) {
        source.Compose(model = model)
        val pending by model.pendingDeletions.collectAsState()
        if (pending.isEmpty()) {
            return
        }
        val snackbar = LocalSnackbarController.current
        LaunchedEffect(pending) {
            val preference = readPreference().removeAll(pending)
            writePreference(preference)
            model.removePendingDeletion(pending)
            snackbar.show("Removed ${pending.first().rate.currency.displayName}")
        }
    }

    private suspend fun readPreference(): ExchangeRatePreference {
        return withContext(Dispatchers.IO) {
            reader.read()
        }
    }

    private suspend fun writePreference(preference: ExchangeRatePreference) {
        withContext(Dispatchers.IO) {
            writer.write(preference)
        }
    }

    private fun ExchangeRatePreference.removeAll(pending: List<CurrencyModel>): ExchangeRatePreference {
        val currencies = selectedCurrencies.toHashSet()
        currencies -= pending.map { it.rate.currency }.toSet()
        return copy(selectedCurrencies = currencies)
    }

}