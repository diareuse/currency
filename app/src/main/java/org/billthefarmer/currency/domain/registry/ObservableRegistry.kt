package org.billthefarmer.currency.domain.registry

import kotlinx.coroutines.flow.StateFlow

interface ObservableRegistry<T> {

    fun get(): StateFlow<List<T>>
    fun add(items: List<T>)
    fun remove(items: List<T>)

}

fun <T> ObservableRegistry<T>.remove(item: T) =
    remove(listOf(item))

fun <T> ObservableRegistry<T>.add(item: T) =
    add(listOf(item))

fun <T> observableRegistryOf(vararg items: T): ObservableRegistry<T> {
    return ObservableRegistryDefault<T>().also {
        it.add(items.toList())
    }
}