package store.model

data class Product(
    val name: String,
    var price: Int,
    var entireQuantity: Int,
    var promotionQuantity: Int,
    var promotion: String?
) {
}
