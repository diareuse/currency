package wiki.depasquale.currency.platform.widget

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import cursola.rate.convert.Conversion
import cursola.rate.convert.Money
import cursola.rate.convert.MoneyFormat
import wiki.depasquale.currency.R
import wiki.depasquale.currency.platform.link.ConverterLaunchIntent
import wiki.depasquale.currency.platform.link.CurrencyPair
import java.util.Locale

@Composable
internal fun FavoritesWidgetContent(
    snapshot: FavoritesSnapshot,
    format: MoneyFormat,
    launchIntent: ConverterLaunchIntent
) {
    val locale = Locale.getDefault()
    Column(
        GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.widgetBackground)
            .cornerRadius(16.dp)
            .padding(12.dp)
            .clickable(actionStartActivity(launchIntent.create(null)))
    ) {
        when (snapshot) {
            is FavoritesSnapshot.Converted -> {
                Header(snapshot.source, format, locale)
                LazyColumn {
                    items(snapshot.conversions) { ConversionRow(it, format, locale, launchIntent) }
                }
            }

            is FavoritesSnapshot.NoFavorites -> {
                Header(snapshot.source, format, locale)
                Message(R.string.widget_no_favorites)
            }

            is FavoritesSnapshot.Unavailable -> {
                snapshot.source?.let { Header(it, format, locale) }
                Message(R.string.widget_unavailable)
            }
        }
    }
}

@Composable
private fun Header(source: Money, format: MoneyFormat, locale: Locale) {
    Text(
        text = format.format(source, locale),
        style = TextStyle(
            color = GlanceTheme.colors.onSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        ),
        modifier = GlanceModifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun Message(@StringRes message: Int) {
    Text(
        text = LocalContext.current.getString(message),
        style = TextStyle(color = GlanceTheme.colors.onSurfaceVariant)
    )
}

@Composable
private fun ConversionRow(
    conversion: Conversion,
    format: MoneyFormat,
    locale: Locale,
    launchIntent: ConverterLaunchIntent
) {
    val pair = CurrencyPair(conversion.source.currency, conversion.result.currency)
    Row(
        GlanceModifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(actionStartActivity(launchIntent.create(pair)))
    ) {
        Text(
            text = conversion.result.currency.currencyCode,
            style = TextStyle(color = GlanceTheme.colors.onSurfaceVariant)
        )
        Spacer(GlanceModifier.defaultWeight())
        Text(
            text = format.format(conversion.result, locale),
            style = TextStyle(color = GlanceTheme.colors.onSurface, fontWeight = FontWeight.Medium)
        )
    }
}
