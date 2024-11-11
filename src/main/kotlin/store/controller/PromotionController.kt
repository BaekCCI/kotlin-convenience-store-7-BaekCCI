package store.controller

import store.model.Product
import store.model.ProductManagement
import store.model.Purchase
import store.view.InputView
import store.view.OutputView

class PromotionController(
    private val items: Purchase,
    private val productManagement: ProductManagement
) {
    private val inputView = InputView()
    private val outputView = OutputView()
    private var promotionItems: Map<Product, Int> = mapOf()

    fun startCheckPromotion() {
        checkPromotion(items.get())


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
            confirmNoDisCount(product.name, normalQuantity)
        } else {
            checkAddExtraItem(product, quantity)
        }
    }

    fun checkAddExtraItem(product: Product, quantity: Int) {
        val promotion = product.promotion
        if (promotion != null) {
            val remainder = quantity % (promotion.buy + promotion.get)

            if (remainder == promotion.buy) {
                isPossibleAddExtraItem(product, quantity)
            } else if (remainder < promotion.buy) {
                confirmNoDisCount(product.name, remainder)
            }
        }
    }

    fun isPossibleAddExtraItem(product: Product, quantity: Int) {
        val promotion = product.promotion
        if (promotion != null) {
            val totalQuantityAfterAddExtra = quantity + promotion.get
            if (totalQuantityAfterAddExtra > product.quantity) {
                val remainder = quantity - (product.quantity / (promotion.buy + promotion.get)) * (promotion.buy + promotion.get)
                confirmNoDisCount(product.name, remainder)
            } else {
                addExtraItem(product.name)
            }
        }
    }

    fun confirmNoDisCount(name: String, remainder: Int) {
        val input = getConfirmNoDisCount(name, remainder)
        if (input == "y") {
            //일반재고를 차감
        } else if (input == "n") {
            //
        }
    }

    //할인 불가 -> 일반결제
    fun getConfirmNoDisCount(name: String, remainder: Int): String {
        while (true) {
            try {
                val input = inputView.confirmNoDiscount(name, remainder).lowercase()
                require(validYesOrNo(input)) { "[ERROR] 잘못된 입력입니다. 다시 입력해 주세요." }
                return input
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    //증정 상품 추가
    fun addExtraItem(name: String) {
        val input = askAddExtraItem(name)
        if (input == "y") {

        } else if (input == "n") {

        }
    }

    fun askAddExtraItem(name: String): String {
        while (true) {
            try {
                val input = inputView.addExtraItem(name).lowercase()
                require(validYesOrNo(input)) { "[ERROR] 잘못된 입력입니다. 다시 입력해 주세요." }
                return input
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    fun validYesOrNo(input: String): Boolean {
        if (input == "y" || input == "n") return true
        return false
    }

}