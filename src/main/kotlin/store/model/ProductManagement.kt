package store.model

const val INVENTORY_PATH = "src/main/resources/products.md"

//재고 관리 클래스
class ProductManagement() {
    private val products: List<Product>

    init {
        val inventory = java.io.File(INVENTORY_PATH).readLines().drop(1)
        products = inventory.map { getProductInfo(it) }
    }

    private fun getProductInfo(productInfo: String): Product {
        val product = productInfo.split(",").map { it.trim() }
        val promotionQuantity = if (product[3] != "null") product[3].toInt() else 0

        return Product(
            name = product[0],
            price = product[1].toInt(),
            entireQuantity = product[2].toInt() + promotionQuantity,
            promotionQuantity = promotionQuantity,
            promotion = product[3]
        )
    }

    fun checkItemInInventory(name: String): Boolean {
        return products.any { it.name == name }
    }

    fun checkItemQuantity(name: String, quantity: Int): Boolean {
        val product = products.find { it.name == name }
        return product?.entireQuantity?.let { it >= quantity } ?: false
    }


    fun updateProductInfo() { //재고 업데이트

    }

}