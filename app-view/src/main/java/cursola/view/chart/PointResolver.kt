package cursola.view.chart

import java.util.Collections

interface HorizontalPointResolver {

    fun get(index: Int): Float

    companion object {

        private val resolvers = mutableMapOf<Definition, HorizontalPointResolver>()

        operator fun invoke(
            stride: Float,
            offset: Float
        ) = resolvers.getOrPut(Definition(stride, offset)) {
            var out: HorizontalPointResolver
            out = HorizontalPointResolverImpl(stride, offset)
            out = HorizontalPointResolverCache(out)
            out
        }

        data class Definition(
            val stride: Float,
            val offset: Float
        )

    }
}

class HorizontalPointResolverImpl(
    private val stride: Float, // space between points
    private val offset: Float
) : HorizontalPointResolver {

    override fun get(index: Int): Float {
        return stride * index + offset
    }

}

class HorizontalPointResolverCache(
    private val origin: HorizontalPointResolver
) : HorizontalPointResolver {
    private val cache = Collections.synchronizedMap(mutableMapOf<Int, Float>())
    override fun get(index: Int): Float {
        return synchronized(cache) {
            cache.getOrPut(index) { origin.get(index) }
        }
    }
}

interface VerticalPointResolver {

    fun get(sample: Double): Float

    companion object {

        operator fun invoke(
            floor: Double,
            ceiling: Double,
            height: Float
        ): VerticalPointResolver {
            var out: VerticalPointResolver
            out = VerticalPointResolverImpl(floor, ceiling)
            out = VerticalPointResolverCache(out)
            out = VerticalPointResolverMultiplicative(out, height)
            return out
        }

    }
}

class VerticalPointResolverImpl(
    private val floor: Double,
    ceiling: Double,
) : VerticalPointResolver {

    private val center = ceiling - floor

    override fun get(sample: Double): Float {
        return 1 - ((sample.toFloat() - floor) / center).toFloat()
    }

}

class VerticalPointResolverCache(
    private val origin: VerticalPointResolver
) : VerticalPointResolver {
    private val cache = Collections.synchronizedMap(mutableMapOf<Double, Float>())
    override fun get(sample: Double): Float {
        return synchronized(cache) {
            cache.getOrPut(sample) { origin.get(sample) }
        }
    }
}

class VerticalPointResolverMultiplicative(
    private val origin: VerticalPointResolver,
    private val value: Float
) : VerticalPointResolver {
    override fun get(sample: Double): Float {
        return origin.get(sample) * value
    }
}