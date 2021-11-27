package org.billthefarmer.currency.tooling

import androidx.annotation.CallSuper
import org.junit.After
import org.junit.Before
import org.mockito.MockitoAnnotations

abstract class MockableTest {

    @Before
    @CallSuper
    open fun prepare() {
        MockitoAnnotations.openMocks(this).close()
    }

    @After
    open fun tearDown() = Unit

}