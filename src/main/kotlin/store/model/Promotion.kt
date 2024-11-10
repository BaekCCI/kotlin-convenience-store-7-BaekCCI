package store.model

data class Promotion(
    val name: String,
    val buy: Int,
    val get: Int,
    val start_date: String,
    val end_date: String
) {}
