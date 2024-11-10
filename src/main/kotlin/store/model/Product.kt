package store.model

data class Product(
    val name: String,
    var price: Int,
    var quantity: Int,
    var promotion: Promotion?
) {
}
