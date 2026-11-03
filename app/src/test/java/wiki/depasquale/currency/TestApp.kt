package wiki.depasquale.currency

import coil.ImageLoader
import coil.ImageLoaderFactory
import okhttp3.OkHttpClient
import wiki.depasquale.currency.di.AppEntryPoint
import wiki.depasquale.currency.di.TestAppEntryPoint
import wiki.depasquale.currency.di.invoke
import java.io.IOException

class TestApp : App(), ImageLoaderFactory {

    override fun createGraph(): AppEntryPoint = TestAppEntryPoint(this)

    override fun newImageLoader(): ImageLoader = ImageLoader.Builder(this)
        .okHttpClient { OkHttpClient.Builder().addInterceptor { throw IOException("offline") }.build() }
        .build()

}
