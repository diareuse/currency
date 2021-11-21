package org.billthefarmer.currency.tooling

import org.junit.After
import org.junit.Before
import org.mockito.MockitoAnnotations

abstract class MockableTest {

    @Before
    fun prepareMocks() {
        MockitoAnnotations.openMocks(this).close()
    }

    @Before
    abstract fun prepare()

    @After
    open fun tearDown() = Unit

}