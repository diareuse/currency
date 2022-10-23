package cursola.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

@Composable
fun CurrencyInput(
    value: Double,
    onValueChanged: (Double) -> Unit,
    currency: Currency,
    locale: Locale,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = remember(locale) {
        NumberFormat.getNumberInstance(locale)
    }
    BasicTextField(
        modifier = modifier,
        value = formatter.format(value),
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.End,
            fontWeight = FontWeight.ExtraBold
        ),
        onValueChange = {
            onValueChanged(formatter.parse(it)?.toDouble() ?: 0.0)
        },
        decorationBox = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    currency.getSymbol(locale),
                    style = LocalTextStyle.current.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                it()
            }
        },
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MaterialTheme {
        var state by remember {
            mutableStateOf(1.0)
        }
        ProvideTextStyle(value = MaterialTheme.typography.displaySmall) {
            CurrencyInput(
                value = state,
                onValueChanged = { state = it },
                currency = Currency.getInstance("CZK"),
                locale = Locale.US,
                onDone = {}
            )
        }
    }
}