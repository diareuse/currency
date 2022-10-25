package cursola.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import java.util.Currency

@Composable
fun CurrencyFlag(
    currency: Currency,
    modifier: Modifier = Modifier,
) {
    val language = stringResource(id = currency.asId()).lowercase()
    SubcomposeAsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data("https://flagicons.lipis.dev/flags/4x3/$language.svg")
            .decoderFactory(SvgDecoder.Factory())
            .crossfade(true)
            .build(),
        loading = {
            CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 4.dp)
        },
        contentScale = ContentScale.Crop,
        contentDescription = currency.currencyCode
    )

}

@SuppressLint("DiscouragedApi")
@Composable
private fun Currency.asId(): Int {
    val context = LocalContext.current
    val res = context.resources
    val id = res.getIdentifier(currencyCode.uppercase(), "string", context.packageName)
    if (id == 0) {
        return R.string.EUR
    }
    return id
}
