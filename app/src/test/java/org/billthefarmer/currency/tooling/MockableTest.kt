package org.billthefarmer.currency.tooling

import org.junit.Before
import org.mockito.MockitoAnnotations

abstract class MockableTest {

    @Before
    fun prepareMocks() {
        MockitoAnnotations.initMocks(this)
    }

}