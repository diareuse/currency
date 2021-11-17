package org.billthefarmer.composition.instance


@JvmInline
value class Parameters(
    private val values: Array<out Any?>
) {

    inline operator fun <reified T> component1(): T = getComponentAt(0, T::class.java)
    inline operator fun <reified T> component2(): T = getComponentAt(1, T::class.java)
    inline operator fun <reified T> component3(): T = getComponentAt(2, T::class.java)
    inline operator fun <reified T> component4(): T = getComponentAt(3, T::class.java)

    @Suppress("UNCHECKED_CAST")
    fun <T> getComponentAt(index: Int, type: Class<T>): T {
        if (values.size <= index) {
            throw ParametersNotSpecifiedException(
                "Expected parameter at $index, but got only ${values.size} parameters"
            )
        }
        val value = values[index]
        if (value == null)
            return value as T

        if (type.isAssignableFrom(value::class.java))
            return value as T

        throw TypeMismatchException(ClassCastException("Casting ${value::class.java} to $type is not possible"))
    }

    companion object {

        val Empty = Parameters(emptyArray())

        operator fun invoke(vararg values: Any?) = Parameters(values)

    }

}