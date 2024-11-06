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
        //검증 로직 추가해야하는감..
        return Product(
            name = product[0],
            price = product[1].toInt(),
            quantity = product[2].toInt(),
            promotion = if (product[3] == "null") "" else product[3]
        )
    }

    //상품 구매 시 상품 재고 업데이트 로직
}