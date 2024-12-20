package store.model

const val ITEM_REGEX = "^\\[[^,\\[\\]]+-\\d+](,\\[[^,\\[\\]]+-\\d+])*?$"
const val INVALID_FORMAT_ERROR = "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."
const val PRODUCT_NOT_FOUND_ERROR = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요."
const val EXCEED_STOCK_ERROR = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."

class Purchase(val items: String, val productManagement: ProductManagement) {
    private val cart: MutableMap<String, Int> = mutableMapOf()

    init {
        require(validateItems()) { INVALID_FORMAT_ERROR }
        addItemToCart()
    }

    private fun validateItems(): Boolean {
        val regex = Regex(ITEM_REGEX)
        val trimmedItems = items.replace(" ", "")
        return trimmedItems.matches(regex)
    }

    private fun addItemToCart() {
        val eachItem = parseItem()
        val tempCart = buildTempCart(eachItem)
        updateCart(tempCart)
    }

    private fun parseItem(): List<String> {
        val cleanedItems = items.replace("[", "").replace("]", "")
        return cleanedItems.split(",").map { it.trim() }
    }

    private fun buildTempCart(eachItem: List<String>): MutableMap<String, Int> {
        val tempCart: MutableMap<String, Int> = mutableMapOf()
        eachItem.forEach {
            val itemParts = it.split("-").map { it.trim() }
            val name = itemParts[0]
            val quantity = itemParts[1].toInt()
            require(productManagement.checkItemInInventory(name)) { PRODUCT_NOT_FOUND_ERROR }
            tempCart[name] = tempCart.getOrDefault(name, 0) + quantity
        }
        return tempCart
    }

    private fun updateCart(tempCart: MutableMap<String, Int>) {
        tempCart.forEach { (name, quantity) ->
            require(
                productManagement.checkItemQuantity(
                    name,
                    quantity
                )
            ) { EXCEED_STOCK_ERROR }
            cart[name] = cart.getOrDefault(name, 0) + quantity
        }
    }

    fun get(): MutableMap<String, Int> {
        return cart
    }

    fun cancelPurchase(name: String, quantity: Int) {
        println(cart)
        if (cart.containsKey(name)) {
            val currentQuantity = cart[name] ?: 0
            cart[name] = currentQuantity - quantity
        }
        println(cart)
    }

    fun addPurchase(name: String, quantity: Int) {
        if (cart.containsKey(name)) {
            val currentQuantity = cart[name] ?: 0
            cart[name] = currentQuantity + quantity
        }
    }

}