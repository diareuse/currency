package org.billthefarmer.currency.domain.preference

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType

class SharedPreferenceReader<T : Any>(
    private val provider: SharedPreferenceProvider,
    private val modelClass: KClass<T>
) : PreferenceReader<T> {

    override fun read(): T {
        val values = provider.getSharedPreferences().all
        val constructor = modelClass.constructors.first()
        val params = constructor.parameters
            .map { createValueFrom(values, it) }
            .toTypedArray()
        return constructor.call(*params)
    }

    private fun createValueFrom(values: Map<String, *>, parameter: KParameter): Any? {
        val key = parameter.findAnnotation<PreferenceKey>()
        requireNotNull(key) {
            "All constructor parameters of $modelClass must be annotated with @PreferenceKey(…)"
        }
        val value = values[key.name]
        if (value == null) {
            return value ?: key.defaultValueProvider.createInstance().getDefault()
        }

        return getValue(value, parameter.type, key)
    }

    private fun getValue(value: Any, parameterType: KType, key: PreferenceKey): Any? {
        fun getValueFromSet(): Set<Any?> {
            val mainArgumentType = parameterType.arguments.first().type
            return (value as Set<Any?>)
                .asSequence()
                .map {
                    if (it == null) null
                    else getValue(it, requireNotNull(mainArgumentType), key)
                }
                .toSet()
        }

        return when (parameterType) {
            String::class.starProjectedType -> value as String?
            Int::class.starProjectedType -> value as Int?
            Long::class.starProjectedType -> value as Long?
            Float::class.starProjectedType -> value as Float?
            Boolean::class.starProjectedType -> value as Boolean?
            else -> when {
                value::class.starProjectedType.isSubtypeOf(Set::class.starProjectedType) -> getValueFromSet()
                else -> key.adapter.createInstance().fromPreference(value)
            }
        }
    }

}