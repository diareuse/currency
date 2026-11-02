package wiki.depasquale.currency

import wiki.depasquale.currency.di.AppEntryPoint
import wiki.depasquale.currency.di.TestAppEntryPoint
import wiki.depasquale.currency.di.invoke

class TestApp : App() {

    override fun createGraph(): AppEntryPoint = TestAppEntryPoint(this)

}
