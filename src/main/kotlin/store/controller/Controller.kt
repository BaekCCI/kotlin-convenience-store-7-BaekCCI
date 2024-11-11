package store.controller

import store.Validator
import store.model.*
import store.view.InputView
import store.view.OutputView

class Controller {
    private val inputView = InputView()
    private val outputView = OutputView()
    private val promotionManagement = PromotionManagement()
    private val productManagement = ProductManagement(promotionManagement)
    private val receipt = Receipt()
    private val validator = Validator()

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
                validator.validYesOrNo(input)
                return input
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

}