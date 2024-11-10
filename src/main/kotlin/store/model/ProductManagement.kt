package store.model

const val INVENTORY_PATH = "src/main/resources/products.md"

//재고 관리 클래스
class ProductManagement(private val promotionManagement: PromotionManagement) {
    private val products: List<Product>

    init {
        val inventory = java.io.File(INVENTORY_PATH).readLines().drop(1)
        products = inventory.map { getProductInfo(it) }
    }

    private fun getProductInfo(productInfo: String): Product {
        val product = productInfo.split(",").map { it.trim() }
        val promotion: Promotion? = promotionManagement.get(product[3])

        return Product(
            name = product[0],
            price = product[1].toInt(),
            quantity = product[2].toInt(),
            promotion = promotion
        )
    }

    fun get(): List<Product> {
        return products
    }

    fun checkItemInInventory(name: String): Boolean {
        return products.any { it.name == name }
    }

    fun checkItemQuantity(name: String, quantity: Int): Boolean {
        var entireQuantity = 0
        products.forEach {
            if (it.name == name) entireQuantity += it.quantity
        }
        if (quantity <= entireQuantity) return true
        return false
    }

    fun checkIsPromotion(name: String, quantity: Int): MutableMap<Product, Int> {
        val promotionItems: MutableMap<Product, Int> = mutableMapOf()
        products.forEach {
            if (it.name == name && it.promotion != null && promotionManagement.checkPromotionDate(it.promotion!!)) {
                promotionItems[it] = quantity
            }
        }
        return promotionItems
    }

    fun updateProductInfo() { //재고 업데이트

    }

}