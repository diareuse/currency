package wiki.depasquale.currency.screen.history

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import wiki.depasquale.currency.R

@Composable
fun ChartMetadataItem(
    max: Double?,
    min: Double?,
    modifier: Modifier = Modifier,
) {
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
            text = "%.2f".format(min),
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.size(8.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_max),
            contentDescription = null,
            modifier = Modifier.size(12.dp)
        )
        Text(text = "%.2f".format(max), style = MaterialTheme.typography.labelSmall)
    }
}