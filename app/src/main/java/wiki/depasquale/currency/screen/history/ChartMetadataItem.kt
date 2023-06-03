package wiki.depasquale.currency.screen.history

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.*
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
            text = formatter.format(if (min?.isNaN() == false) min else 0.0),
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.size(8.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_max),
            contentDescription = null,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = formatter.format(if (max?.isNaN() == false) max else 0.0),
            style = MaterialTheme.typography.labelSmall
        )
    }
}