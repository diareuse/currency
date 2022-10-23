package cursola.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ExchangeRateItem(
    icon: @Composable () -> Unit,
    flag: @Composable () -> Unit,
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp, 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        flag()
        text()
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ExchangeRateItem(
        icon = {
            Image(painterResource(android.R.drawable.ic_delete), null)
        },
        flag = {
            Image(painterResource(android.R.drawable.arrow_up_float), null)
        },
        text = {
            Column {
                Text("US Dollar")
                CurrencyValue("$123.45", "$")
            }
        },
        modifier = Modifier.clickable {}
    )
}