package wiki.depasquale.currency.screen.favorite

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cursola.view.CurrencyInput
import wiki.depasquale.currency.screen.style.CurrencyTheme
import java.util.Currency
import java.util.Locale

@Composable
fun FavoriteInputField(
    value: String,
    selected: Currency,
    locale: Locale,
    onValueChanged: (String) -> Unit,
    onCloseKeyboard: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ProvideTextStyle(
        value = MaterialTheme.typography.displaySmall.copy(
            color = LocalContentColor.current
        )
    ) {
        CurrencyInput(
            modifier = modifier.padding(24.dp),
            value = value,
            onValueChanged = onValueChanged,
            currency = selected,
            locale = locale,
            onDone = onCloseKeyboard
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CurrencyTheme {
        FavoriteInputField(
            value = "0.0",
            selected = Currency.getAvailableCurrencies().random(),
            locale = Locale.getDefault(),
            onValueChanged = {},
            onCloseKeyboard = { /*TODO*/ })
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun Preview_Night() {
    CurrencyTheme {
        FavoriteInputField(
            value = "0.0",
            selected = Currency.getAvailableCurrencies().random(),
            locale = Locale.getDefault(),
            onValueChanged = {},
            onCloseKeyboard = { /*TODO*/ })
    }
}