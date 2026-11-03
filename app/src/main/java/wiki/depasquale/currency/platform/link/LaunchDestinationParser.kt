package wiki.depasquale.currency.platform.link

fun interface LaunchDestinationParser {

    fun parse(link: String?): LaunchDestination

}
