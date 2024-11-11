package store.model

class Receipt {
    val generalProduct: MutableMap<Product, Int> = mutableMapOf()
    val promotionProduct: MutableMap<Product, Int> = mutableMapOf()
    var totalPrice = 0
    var discountPromotion = 0
    var resultPrice = 0

    fun addPromotion(product: Product, quantity: Int) {
        promotionProduct[product] = promotionProduct.getOrDefault(product, 0) + quantity
    }

    fun setGeneralProduct(cart: MutableMap<String, Int>, productManagement: ProductManagement) {
        val products = productManagement.get()
        cart.forEach { (name, quantity) ->
            val product = products.find { it.name == name }
            if (product != null) {
                val promotionQuantity = promotionProduct[product] ?: 0
                val remainQuantity = quantity - promotionQuantity

                generalProduct[product] = remainQuantity
            }
        }
    }


}
