package store.model

const val INVENTORY_PATH = "src/main/resources/products.md"

//재고 관리 클래스
class ProductManagement(private val promotionManagement: PromotionManagement) {
    private var products: MutableList<Product> = mutableListOf()

    init {
        val inventory = java.io.File(INVENTORY_PATH).readLines().drop(1)
        products = inventory.map { getProductInfo(it) }.toMutableList()
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

    fun getPromotionItems(name: String, quantity: Int): Product? {
        products.forEach {
            if (it.name == name && it.promotion != null && promotionManagement.checkPromotionDate(it.promotion!!)) {
                return it
            }
        }
        return null
    }

    fun updateProduct(receipt: Receipt) { //재고 업데이트
        receipt.entireProduct.forEach { (product, quantity) ->
            val productInInventory = products.find { it.name == product.name && it.promotion == product.promotion }
            if (productInInventory != null) {
                if (productInInventory.quantity - quantity < 0) {
                    handleGeneralProduct(product, quantity - productInInventory.quantity)
                    productInInventory.quantity = 0
                } else productInInventory.quantity -= quantity
            }
        }
    }

    private fun handleGeneralProduct(product: Product, quantity: Int) {
        val productInInventory = products.find { it.name == product.name && it.promotion == null }
        if (productInInventory != null) {
            productInInventory.quantity -= quantity
        }
    }

}