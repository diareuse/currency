package wiki.depasquale.currency.screen.history

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import wiki.depasquale.currency.R
import java.text.NumberFormat
import java.util.Currency

@Composable
fun ChartMetadataItem(
    max: Double?,
    min: Double?,
    currency: Currency,
    modifier: Modifier = Modifier,
) {
    if (max?.isNaN() == true || min?.isNaN() == true) return
    val formatter = remember(currency) {
        val format = NumberFormat.getCurrencyInstance()
        format.currency = currency
        format
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_min),
            contentDescription = null,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = formatter.format(min),
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.size(8.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_max),
            contentDescription = null,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = formatter.format(max),
            style = MaterialTheme.typography.labelSmall
        )
    }
}