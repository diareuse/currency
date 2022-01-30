package wiki.depasquale.currency.presentation.effect

interface AnimatedIndicationSource {

    val progress: Float

    suspend fun animatePress()
    suspend fun animateRelease()

}