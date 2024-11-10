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
        if (product.quantity < quantity) {
            val normalQuantity = quantity - product.quantity
            confirmListPrice(product.name, normalQuantity)
        } else {

        }
    }

    fun isPossibleAddExtraItem() {

    }

    //일반결제 유도
    fun confirmListPrice(name: String, quantity: Int) {
        //yes or no -> 멤버십
    }

    fun isNeedExtraItem(cart: MutableMap<String, Int>) {

    }

}