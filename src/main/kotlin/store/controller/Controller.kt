package store.controller

import store.model.Product
import store.model.ProductManagement
import store.model.PromotionManagement
import store.model.Purchase
import store.view.InputView
import store.view.OutputView

const val ITEM_REGEX = "^\\[\\S+-\\d+](,\\[\\S+-\\d+])*?$|^\\S+-\\d+$"


class Controller {
    private val inputView = InputView()
    private val outputView = OutputView()
    private val promotionManagement = PromotionManagement()
    private val productManagement = ProductManagement(promotionManagement)

    fun start() {
        do {
            // 구매 로직

            displayProduct()
            val items = getItems()
            val promotionController = PromotionController(items, productManagement)
            promotionController.startCheckPromotion()

        } while (inputView.checkAdditionalPurchase() == "Y" || inputView.checkAdditionalPurchase() == "y")
    }
    fun displayProduct(){
        val products = productManagement.get()
        outputView.printWelcome()
        products.forEach {
            outputView.printProducts(it.name,it.price,it.quantity, it.promotion?.name)
        }
    }

    fun getItems(): Purchase {
        while (true) {
            try {
                val items = inputView.readItem()
                return Purchase(items, productManagement)
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

}