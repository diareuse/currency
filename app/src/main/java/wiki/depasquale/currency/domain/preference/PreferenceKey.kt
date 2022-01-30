package wiki.depasquale.currency.domain.preference

import androidx.annotation.Keep
import kotlin.reflect.KClass

@Keep
@Retention(AnnotationRetention.RUNTIME)
annotation class PreferenceKey(
    val name: String,
    val adapter: KClass<out PreferenceAdapter> = PreferenceAdapter::class,
    val defaultValueProvider: KClass<out ValueProvider> = ValueProvider::class
)