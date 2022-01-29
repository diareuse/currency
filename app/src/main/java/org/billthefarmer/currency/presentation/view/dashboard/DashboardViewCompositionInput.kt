package org.billthefarmer.currency.presentation.view.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.billthefarmer.currency.R
import org.billthefarmer.currency.presentation.gesture.DismissDirectionVertical
import org.billthefarmer.currency.presentation.gesture.dismissVertically
import org.billthefarmer.currency.screen.dashboard.DashboardViewModel
import org.billthefarmer.currency.screen.style.shapes
import java.util.*
import kotlin.math.max

class DashboardViewCompositionInput : DashboardViewComposition {

    @Composable
    override fun Compose(model: DashboardViewModel) {
        val amount by model.amount.collectAsState()
        val currencyState = model.selectedCurrency.collectAsState()
        val currency = currencyState.value ?: return
        val context = LocalContext.current

        Compose(
            flagResource = currency.getFlagResource(context),
            currency = currency.rate.currency,
            value = amount,
            onValueChange = { model.amount.value = it },
            onDismiss = { model.selectedCurrency.value = null }
        )
    }

    @Composable
    private fun Compose(
        flagResource: Int,
        currency: Currency,
        value: String,
        onValueChange: (String) -> Unit,
        onDismiss: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .dismissVertically(
                    dismissDirection = DismissDirectionVertical.Up,
                    onDismiss = onDismiss
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(95.dp)
                    .height(56.dp),
                painter = painterResource(id = flagResource)
            )
            Spacer(modifier = Modifier.size(32.dp))
            Input(
                modifier = Modifier.weight(1f),
                currency = currency,
                value = value,
                onValueChange = onValueChange
            )
        }
    }

    @Composable
    private fun Image(modifier: Modifier, painter: Painter) {
        Surface(
            modifier = modifier
                .testTag("input-flag"),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = "flag"
            )
        }
    }

    @Composable
    private fun Input(
        modifier: Modifier,
        currency: Currency,
        value: String,
        onValueChange: (String) -> Unit
    ) {
        Surface(
            modifier = modifier
                .testTag("input-text"),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            val textColor = LocalContentColor.current
            val textStyle = LocalTextStyle.current
            BasicTextField(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                value = value,
                onValueChange = onValueChange,
                textStyle = textStyle.copy(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = remember(currency, textColor) {
                    createTransformation(currency, textColor)
                }
            )
        }
    }

    private fun createTransformation(currency: Currency, textColor: Color): VisualTransformation {
        return VisualTransformation {
            val fragmentCurrency = AnnotatedString(
                currency.symbol,
                SpanStyle(color = textColor.copy(alpha = .3f))
            )
            val fragmentNumbers = it
            val jointString = fragmentCurrency + fragmentNumbers
            val offset = object : OffsetMapping {
                override fun originalToTransformed(offset: Int) =
                    offset + fragmentCurrency.length

                override fun transformedToOriginal(offset: Int) =
                    max(0, offset - fragmentCurrency.length)
            }
            TransformedText(jointString, offset)
        }
    }

    // ---

    @Preview
    @Composable
    private fun Preview() {
        var value by remember {
            mutableStateOf("123")
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(32.dp)
        ) {
            Compose(
                flagResource = R.drawable.flag_aud,
                currency = Currency.getInstance("USD"),
                value = value,
                onValueChange = { value = it },
                onDismiss = {}
            )
        }
    }

}