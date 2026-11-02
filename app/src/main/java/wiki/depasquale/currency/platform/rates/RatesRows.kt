package wiki.depasquale.currency.platform.rates

data class RatesRows(
    val columns: List<String>,
    val rows: List<List<Any?>>
) {

    fun project(projection: List<String>?): RatesRows {
        if (projection == null) return this
        val indices = projection.map { column ->
            columns.indexOf(column).also { require(it >= 0) { "Unknown column $column" } }
        }
        return RatesRows(projection, rows.map { row -> indices.map(row::get) })
    }

}
