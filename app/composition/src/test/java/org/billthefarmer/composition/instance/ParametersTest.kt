package org.billthefarmer.composition.instance

import com.google.common.truth.Truth
import org.billthefarmer.composition.tooling.getNoopScope
import org.junit.Test

class ParametersTest {

    @Test
    fun `returns value when used with parameters`() {
        val creator = SingleCreator { (value: Int) -> Value(value) }
        val scope = getNoopScope()

        val value = creator.getValue(scope, Parameters(1))
        Truth.assertThat(value).isEqualTo(Value(1))
    }

    @Test
    fun `returns value even if provided with more parameters`() {
        val creator = SingleCreator { (value: Int) -> Value(value) }
        val scope = getNoopScope()
        val value = creator.getValue(scope, Parameters(1, 2, 3))

        Truth.assertThat(value).isEqualTo(Value(1))
    }

    @Test
    fun `returns value even if provided with null`() {
        val creator = SingleCreator { (value: Int?) -> Value(value ?: -1) }
        val scope = getNoopScope()
        val value = creator.getValue(scope, Parameters(null))

        Truth.assertThat(value).isEqualTo(Value(-1))
    }

    @Test(expected = TypeMismatchException::class)
    fun `fails when provided with mismatched parameters type`() {
        val creator = SingleCreator { (value: Int) -> Value(value) }
        val scope = getNoopScope()
        creator.getValue(scope, Parameters("1"))
    }

    @Test(expected = ParametersNotSpecifiedException::class)
    fun `fails when provided with no parameters`() {
        val creator = SingleCreator { (value: Int) -> Value(value) }
        val scope = getNoopScope()
        creator.getValue(scope)
    }

    data class Value(
        val int: Int
    )

}