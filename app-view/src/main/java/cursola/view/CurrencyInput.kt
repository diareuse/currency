package cursola.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import java.util.Currency
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyInput(
    value: String,
    onValueChanged: (String) -> Unit,
    currency: Currency,
    locale: Locale,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = TextFieldValue(value, selection = TextRange(value.length)),
        onValueChange = { onValueChanged(it.text.replace(",", ".")) },
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.End,
            fontWeight = FontWeight.Bold
        ),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        maxLines = 1,
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        leadingIcon = {
            Text(
                currency.getSymbol(locale),
                style = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MaterialTheme {
        var state by remember {
            mutableStateOf("1123005023.42")
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