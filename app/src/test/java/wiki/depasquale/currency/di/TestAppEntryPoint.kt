package wiki.depasquale.currency.di

import android.content.Context
import cursola.rate.ConversionRateDataSource
import cursola.rate.ExchangeRate
import cursola.rate.ExchangeRateDataSource
import cursola.rate.ExchangeRateError
import cursola.rate.FavoriteDataSource
import cursola.rate.HistoryDataSource
import cursola.rate.di.ExchangeRateLibrary
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import spike.EntryPoint
import spike.Include
import spike.Singleton
import spike.test.Replaces
import wiki.depasquale.currency.platform.FavoriteDataSourceNotifying
import wiki.depasquale.currency.platform.FavoritesChangedListener
import java.util.Currency
import java.util.Date
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@EntryPoint
interface TestAppEntryPoint : AppEntryPoint {

    val exchangeRates: FixedExchangeRateDataSource
    val favorites: FavoriteDataSource
    val textConversionDeadline: AdjustableTextConversionDeadline
    val quickRateDeadline: AdjustableQuickRateDeadline
    val favoriteReadFailure: FavoriteReadFailure

    @EntryPoint.Factory
    interface Factory {
        fun create(context: Context): TestAppEntryPoint
    }

    companion object

}

@Singleton
@Include
@Replaces(ExchangeRateDataSource::class)
class FixedExchangeRateDataSource : ExchangeRateDataSource {

    @Volatile
    var rates: List<ExchangeRate> = listOf(rate)

    @Volatile
    var hanging = false

    @Volatile
    var failure: Throwable? = null

    @Volatile
    var hung = false

    @Volatile
    var abandoned = false

    @Volatile
    var latency: Duration = Duration.ZERO

    override suspend fun get(): List<ExchangeRate> {
        if (hanging) {
            try {
                hung = true
                awaitCancellation()
            } finally {
                abandoned = true
            }
        }
        delay(latency)
        failure?.let { throw it }
        return rates
    }

    companion object {
        val rate = ExchangeRate(Currency.getInstance("XAU"), 0.42, Date(0))
    }

}

@Include
@Replaces(ConversionRateDataSource::class)
class FixedConversionRateDataSource(
    private val exchange: FixedExchangeRateDataSource
) : ConversionRateDataSource {

    override suspend fun get(from: Currency, to: Currency): Double {
        val rates = exchange.get().associate { it.currency to it.rate }
        val fromRate = rates[from] ?: throw ExchangeRateError.NotFoundException("No rate for $from")
        val toRate = rates[to] ?: throw ExchangeRateError.NotFoundException("No rate for $to")
        return toRate / fromRate
    }

}

@Include
@Replaces(BinderTimeout::class)
fun shortBinderTimeout() = BinderTimeout(100.milliseconds)

@Singleton
@Include
class AdjustableTextConversionDeadline {

    @Volatile
    var value: Duration = 5.seconds

}

@Include
@Replaces(TextConversionDeadline::class)
internal fun adjustableTextConversionDeadline(deadline: AdjustableTextConversionDeadline) =
    TextConversionDeadline(deadline.value)

@Singleton
@Include
class AdjustableQuickRateDeadline {

    @Volatile
    var value: Duration = 2.seconds

}

@Include
@Replaces(QuickRateDeadline::class)
internal fun adjustableQuickRateDeadline(deadline: AdjustableQuickRateDeadline) =
    QuickRateDeadline(deadline.value)

@Include
@Replaces(HistoryDataSource::class)
class EmptyHistoryDataSource : HistoryDataSource {

    override suspend fun get(currency: Currency) = emptyList<ExchangeRate>()

}

@Singleton
@Include
class FavoriteReadFailure {

    @Volatile
    var value: Throwable? = null

}

class ReadFailingFavoriteDataSource(
    private val origin: FavoriteDataSource,
    private val failure: FavoriteReadFailure
) : FavoriteDataSource by origin {

    override suspend fun list(): List<Currency> {
        failure.value?.let { throw it }
        return origin.list()
    }

}

@Include
@Replaces(FavoriteDataSource::class)
internal fun readFailingFavorite(
    @ExchangeRateLibrary source: FavoriteDataSource,
    changed: FavoritesChangedListener,
    failure: FavoriteReadFailure
): FavoriteDataSource = FavoriteDataSourceNotifying(ReadFailingFavoriteDataSource(source, failure), changed)
