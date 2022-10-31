package cursola.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import java.util.Currency

@Composable
fun CurrencyFlag(
    currency: Currency,
    modifier: Modifier = Modifier,
) {
    val language = stringResource(id = currency.asId()).lowercase()
    SubcomposeAsyncImage(
        modifier = modifier.background(MaterialTheme.colorScheme.surface),
        model = ImageRequest.Builder(LocalContext.current)
            .data("https://flagicons.lipis.dev/flags/4x3/$language.svg")
            .decoderFactory(SvgDecoder.Factory())
            .crossfade(true)
            .diskCacheKey("flag-$language")
            .diskCachePolicy(CachePolicy.ENABLED)
            .build(),
        loading = {
            ProgressIndicator(tint = MaterialTheme.colorScheme.primary)
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

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CurrencyFlag(
        currency = Currency.getInstance("USD"),
        modifier = Modifier
            .width(40.dp)
            .height(30.dp)
            .shadow(8.dp)
            .clip(MaterialTheme.shapes.small)
    )
}