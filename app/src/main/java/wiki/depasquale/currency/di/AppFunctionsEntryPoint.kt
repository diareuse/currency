package wiki.depasquale.currency.di

import wiki.depasquale.currency.platform.functions.CurrencyFunctions

interface AppFunctionsEntryPoint {

    val currencyFunctions: CurrencyFunctions

}
