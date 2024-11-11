package store.controller

import store.model.*
import store.view.InputView
import store.view.OutputView

class Controller {
    private val inputView = InputView()
    private val outputView = OutputView()
    private val promotionManagement = PromotionManagement()
    private val productManagement = ProductManagement(promotionManagement)
    private val receipt = Receipt()

    fun start() {
        do {
            displayProduct()
            val items = getItems()
            val promotionController = PromotionController(items, productManagement, receipt)
            promotionController.startCheckPromotion()
            val membershipController = MembershipController(receipt)
            membershipController.startMembership()
            displayReceipt()
            productManagement.updateProduct(receipt)
        } while (checkAdditionalPurchase() == "y")
    }

    private fun displayProduct() {
        val products = productManagement.get()
        outputView.printWelcome()
        products.forEach {
            outputView.printProducts(it.name, it.price, it.quantity, it.promotion?.name)
        }
    }

    private fun displayReceipt() {
        outputView.printReceipt(receipt)
    }

    private fun getItems(): Purchase {
        while (true) {
            try {
                val items = inputView.readItem()
                return Purchase(items, productManagement)
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    private fun checkAdditionalPurchase(): String {
        while (true) {
            try {
                receipt.reset()
                val input = inputView.checkAdditionalPurchase().lowercase()
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