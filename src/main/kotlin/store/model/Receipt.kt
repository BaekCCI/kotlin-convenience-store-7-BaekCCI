package store.model

class Receipt {
    val generalProduct: MutableMap<Product, Int> = mutableMapOf()
    val promotionProduct: MutableMap<Product, Int> = mutableMapOf()
    val extraProduct: MutableMap<Product, Int> = mutableMapOf()
    var totalPrice = 0
    var discountPromotion = 0
    var discountMembership = 0
    var resultPrice = 0

    fun addPromotion(product: Product, quantity: Int) {
        promotionProduct[product] = promotionProduct.getOrDefault(product, 0) + quantity
    }

    fun setReceipt(cart: MutableMap<String, Int>, productManagement: ProductManagement) {
        setGeneralProduct(cart, productManagement)
        addExtraProduct()
        setTotalPrice()
        setDiscountPromotion()
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

    fun addExtraProduct() {
        promotionProduct.forEach { (product, quantity) ->
            val promotion = product.promotion
            extraProduct[product] = quantity / (promotion!!.get + promotion.buy)
        }
    }

    fun setTotalPrice() {
        generalProduct.forEach { (product, quantity) ->
            totalPrice += product.price * quantity
        }
        promotionProduct.forEach { (product, quantity) ->
            totalPrice += product.price * quantity
        }
    }

    fun setDiscountPromotion() {
        extraProduct.forEach { (product, quantity) ->
            discountPromotion += product.price * quantity
        }
    }

    fun setDiscountMembership() {
        generalProduct.forEach { (product, quantity) ->
            discountMembership += (product.price * quantity * 0.3f).toInt()
        }
        if (discountMembership >= 8000) discountMembership = 8000
    }

    fun setResultPrice() {
        resultPrice = totalPrice - discountPromotion - discountMembership.toInt()
    }


}
