package store.model

const val ITEM_REGEX = "^\\[\\S+-\\d+](,\\[\\S+-\\d+])*?$|^\\S+-\\d+$"

class Purchase(val items: String, val productManagement: ProductManagement) {
    val cart: MutableMap<String, Int> = mutableMapOf()

    init {
        require(validateItems()) { "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요." }
        addItemToCart()
    }

    fun validateItems(): Boolean {
        val regex = Regex(ITEM_REGEX)
        val trimmedItems = items.replace(" ", "")
        return trimmedItems.matches(regex)
    }

    fun addItemToCart() {
        val cleanedItems = items.replace("[", "").replace("]", "")
        val eachItem = cleanedItems.split(",").map { it.trim() }
        val tempCart: MutableMap<String, Int> = mutableMapOf()
        eachItem.forEach {
            val itemParts = it.split("-").map { it.trim() }
            val name = itemParts[0]
            val quantity = itemParts[1].toInt()
            require(productManagement.checkItemInInventory(name)) { "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요." }

            tempCart[name] = tempCart.getOrDefault(name, 0) + quantity
        }
        tempCart.forEach { (name, quantity) ->
            require(
                productManagement.checkItemQuantity(
                    name,
                    quantity
                )
            ) { "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요." }
            cart[name] = cart.getOrDefault(name, quantity)
        }
    }

}