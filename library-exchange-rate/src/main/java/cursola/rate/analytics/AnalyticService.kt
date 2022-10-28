package cursola.rate.analytics

interface AnalyticService {

    fun log(event: String, params: Map<String, Any>)

}