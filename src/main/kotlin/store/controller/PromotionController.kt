package store.controller

import store.model.Product
import store.model.ProductManagement
import store.model.Purchase
import store.model.Receipt
import store.view.InputView

class PromotionController(
    private val items: Purchase,
    private val productManagement: ProductManagement
) {
    private val inputView = InputView()
    private var promotionItems: Map<Product, Int> = mapOf()
    private val receipt = Receipt()


    fun startCheckPromotion() {
        val cart = items.get()
        checkPromotion(cart)
    }

    //프로모션 상품이 존재하는지 확인
    fun checkPromotion(cart: MutableMap<String, Int>) {
        cart.forEach { (name, quantity) ->
            promotionItems = productManagement.getPromotionItems(name, quantity)
        }
        if (promotionItems.isNotEmpty()) {
            promotionItems.forEach { (product, quantity) ->
                checkPromotionQuantity(product, quantity)
            }
        }

    }
    //프로모션 상품 구매 수량과 재고 비교
    fun checkPromotionQuantity(product: Product, quantity: Int) {
        var normalQuantity = quantity - product.quantity
        if (normalQuantity > 0) {
            addPromotionProducts(product,quantity)
            confirmNoDisCount(product.name, normalQuantity)
        } else {
            checkAddExtraItem(product, quantity)
        }
    }
    //영수증에 프로모션 아이템 추가
    fun addPromotionProducts(product: Product, quantity: Int){
        val promotion = product.promotion
        val promotionProductQuantity = (product.quantity/(promotion!!.get+ promotion.buy))*(promotion.get+ promotion.buy)
        receipt.addPromotion(product,promotionProductQuantity)
    }
    //프로모션 적용 조건 충족 확인
    fun checkAddExtraItem(product: Product, quantity: Int) {
        val promotion = product.promotion
        if (promotion != null) {
            val remainder = quantity % (promotion.buy + promotion.get)
            if(remainder == 0) receipt.addPromotion(product,quantity)
            else if (remainder == promotion.buy) {
                isPossibleAddExtraItem(product, quantity)
            } else if (remainder < promotion.buy) { //
                confirmNoDisCount(product.name, remainder)
            }
        }
    }
    //증정 가능 유무(상품 증정 시 프로모션 재고 보다 많은가)
    fun isPossibleAddExtraItem(product: Product, quantity: Int) {
        val promotion = product.promotion
        if (promotion != null) {
            val totalQuantityAfterAddExtra = quantity + promotion.get
            if (totalQuantityAfterAddExtra > product.quantity) {
                val remainder = quantity - (product.quantity / (promotion.buy + promotion.get)) * (promotion.buy + promotion.get)
                receipt.addPromotion(product,quantity-remainder)
                confirmNoDisCount(product.name, remainder)
            } else {
                addExtraItem(product,quantity)
            }
        }
    }

    fun confirmNoDisCount(name: String, remainder: Int) {
        val input = getConfirmNoDisCount(name, remainder)
        if (input == "y") {

        } else if (input == "n") {
            //cart에서 삭제
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
    fun addExtraItem(product: Product, quantity : Int) {
        val input = askAddExtraItem(product.name)
        if (input == "y") {
            receipt.addPromotion(product,quantity+product.promotion!!.get)
        } else if (input == "n") {
            //cart에서 삭제
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
        return input == "y" || input == "n"
    }

}