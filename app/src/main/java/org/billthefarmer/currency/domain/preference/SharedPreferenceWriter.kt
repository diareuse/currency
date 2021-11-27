package org.billthefarmer.currency.domain.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.*

class SharedPreferenceWriter<T : Any>(
    private val provider: SharedPreferenceProvider,
    private val modelBlueprint: KClass<T>
) : PreferenceWriter<T> {

    override fun write(preference: T) {
        provider.getSharedPreferences().edit {
            modelBlueprint.memberProperties.forEach {
                writeValue(preference, it)
            }
        }
    }

    private fun SharedPreferences.Editor.writeValue(model: T, property: KProperty1<out T, Any?>) {
        val key: PreferenceKey = property.findAnnotation()
            ?: property.getter.findAnnotation()
            ?: return
        val value = property.getter.call(model)
        val type = property.getter.returnType
        writeValue(value, type, key)
    }

    private fun SharedPreferences.Editor.writeValue(value: Any?, type: KType, key: PreferenceKey) {
        when (type) {
            String::class.starProjectedType -> putString(key.name, value as String?)
            Int::class.starProjectedType -> putInt(key.name, value as Int)
            Long::class.starProjectedType -> putLong(key.name, value as Long)
            Float::class.starProjectedType -> putFloat(key.name, value as Float)
            Boolean::class.starProjectedType -> putBoolean(key.name, value as Boolean)
            else -> {
                val adapter by lazy { key.adapter.createInstance() }
                if (type.isSubtypeOf(Collection::class.starProjectedType)) {
                    val set = (value as Collection<Any?>).asSequence()
                        .filterNotNull()
                        .map {
                            when (it) {
                                is String -> it
                                else -> adapter.toPreference(it) as String
                            }
                        }
                        .toSet()
                    putStringSet(key.name, set)
                } else {
                    val adaptedValue = adapter.toPreference(value)
                    requireNotNull(adaptedValue) {
                        "Custom types cannot be null"
                    }
                    writeValue(adaptedValue, adaptedValue::class.starProjectedType, key)
                }
            }
        }
    }

}