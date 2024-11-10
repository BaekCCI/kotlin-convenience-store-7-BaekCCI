package store.controller

import store.model.Product
import store.model.ProductManagement
import store.model.Purchase
import store.view.InputView

class PromotionController(
    private val items: Purchase,
    private val productManagement: ProductManagement
) {
    private val inputView = InputView()
    private var promotionItems: Map<Product, Int> = mapOf()

    fun startCheckPromotion() {


    }

    //프로모션 상품이 존재하는지 확인
    fun checkPromotion(cart: MutableMap<String, Int>) {
        cart.forEach { (name, quantity) ->
            promotionItems = productManagement.checkIsPromotion(name, quantity)
        }
        if (promotionItems.isNotEmpty()) {
            promotionItems.forEach { (product, quantity) ->
                checkPromotionQuantity(product, quantity)
            }
        }
    }

    //프로모션 상품 구매 수량과 재고 비교
    fun checkPromotionQuantity(product: Product, quantity: Int) {
        val normalQuantity = quantity - product.quantity
        if (normalQuantity > 0) {
            confirmListPrice(product.name, normalQuantity)
        } else {
            addExtraItem(product, quantity)
        }
    }

    fun addExtraItem(product: Product, quantity: Int) {
        val promotion = product.promotion
        if (promotion != null) {
            val remainder = quantity % (promotion.buy + promotion.get)

            if (remainder == promotion.buy) {
                isPossibleAddExtraItem(product, quantity)
            } else if (remainder < promotion.buy) {
                confirmListPrice(product.name, remainder)
            }
        }
    }

    fun isPossibleAddExtraItem(product: Product, quantity: Int) {
        val promotion = product.promotion
        if (promotion != null) {
            val totalQuantityAfterAddExtra = quantity + promotion.get
            if (totalQuantityAfterAddExtra > product.quantity) {
                val normalQuantity = quantity - (product.quantity / (promotion.buy + promotion.get)) * (promotion.buy + promotion.get)
                confirmListPrice(product.name, normalQuantity)
            } else {
                addExtraItem(product.name)
            }
        }
    }

    //일반결제 유도
    fun confirmListPrice(name: String, quantity: Int) {
        //yes or no -> 멤버십
    }

    //증정 상품 추가
    fun addExtraItem(name: String) {

    }

}