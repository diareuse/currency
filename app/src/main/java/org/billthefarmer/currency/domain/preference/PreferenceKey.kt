package org.billthefarmer.currency.domain.preference

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
annotation class PreferenceKey(
    val name: String,
    val adapter: KClass<out PreferenceAdapter> = PreferenceAdapter::class,
    val defaultValueProvider: KClass<out ValueProvider> = ValueProvider::class
)