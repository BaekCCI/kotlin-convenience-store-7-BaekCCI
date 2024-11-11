package store.controller

import store.model.Product
import store.model.ProductManagement
import store.model.Purchase
import store.model.Receipt
import store.view.InputView

class PromotionController(
    private val items: Purchase,
    private val productManagement: ProductManagement,
    private val receipt: Receipt
) {
    private val inputView = InputView()
    private var promotionItems:MutableMap<Product, Int> = mutableMapOf()


    fun startCheckPromotion() {
        val cart = items.get()
        checkPromotion(cart)
        receipt.setReceipt(cart, productManagement)
    }

    //프로모션 상품이 존재하는지 확인
    private fun checkPromotion(cart: MutableMap<String, Int>) {
        cart.forEach { (name, purchaseQuantity) ->
            val product = productManagement.getPromotionItems(name,purchaseQuantity)
            if(product!=null){
                promotionItems[product]=purchaseQuantity
            }
        }
        if (promotionItems.isNotEmpty()) {
            promotionItems.forEach { (product, purchaseQuantity) ->
                checkPromotionQuantity(product, purchaseQuantity)
            }
        }
    }

    //프로모션 상품 구매 수량과 재고 비교
    private fun checkPromotionQuantity(product: Product, purchaseQuantity: Int) {
        var normalQuantity = purchaseQuantity - product.quantity
        if (normalQuantity > 0) {
            addPromotionProducts(product, purchaseQuantity)
            confirmNoDisCount(product, normalQuantity)
        } else {
            checkAddExtraItem(product, purchaseQuantity)
        }
    }

    //영수증에 프로모션 아이템 추가
    private fun addPromotionProducts(product: Product, quantity: Int) {
        val promotion = product.promotion
        val promotionProductQuantity =
            (product.quantity / (promotion!!.get + promotion.buy)) * (promotion.get + promotion.buy)
        receipt.addPromotion(product, promotionProductQuantity)
    }

    //프로모션 적용 조건 충족 확인
    private fun checkAddExtraItem(product: Product, purchaseQuantity: Int) {
        val promotion = product.promotion
        if (promotion != null) {
            val remainder = purchaseQuantity % (promotion.buy + promotion.get)
            if (remainder == 0) receipt.addPromotion(product, purchaseQuantity)
            else if (remainder == promotion.buy) {
                isPossibleAddExtraItem(product, purchaseQuantity)
            } else if (remainder < promotion.buy) { //
                confirmNoDisCount(product, remainder)
            }
        }
    }

    //증정 가능 유무(상품 증정 시 프로모션 재고 보다 많은가)
    private fun isPossibleAddExtraItem(product: Product, quantity: Int) {
        val promotion = product.promotion
        if (promotion != null) {
            val totalQuantityAfterAddExtra = quantity + promotion.get
            if (totalQuantityAfterAddExtra > product.quantity) {
                val remainder =
                    quantity - (product.quantity / (promotion.buy + promotion.get)) * (promotion.buy + promotion.get)
                receipt.addPromotion(product, quantity - remainder)
                confirmNoDisCount(product, remainder)
            } else {
                addExtraItem(product, quantity)
            }
        }
    }

    //할인 불가 -> 일반결제
    private fun confirmNoDisCount(product: Product, remainder: Int) {
        val input = askConfirmNoDisCount(product.name, remainder)
        if (input == "y") {

        } else if (input == "n") {
            items.cancelPurchase(product.name, remainder)
        }
    }

    private fun askConfirmNoDisCount(name: String, remainder: Int): String {
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
    private fun addExtraItem(product: Product, quantity: Int) {
        val input = askAddExtraItem(product.name)
        if (input == "y") {
            receipt.addPromotion(product, quantity + product.promotion!!.get)
            items.addPurchase(product.name, product.promotion!!.get)
        } else if (input == "n") {
            receipt.addPromotion(product, quantity - product.promotion!!.buy)
        }
    }

    private fun askAddExtraItem(name: String): String {
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

    private fun validYesOrNo(input: String): Boolean {
        return input == "y" || input == "n"
    }

}