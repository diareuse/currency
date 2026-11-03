package wiki.depasquale.currency.platform.link

sealed interface LaunchDestination {

    data object Start : LaunchDestination

    data object History : LaunchDestination

    data class Convert(val pair: CurrencyPair) : LaunchDestination

}
