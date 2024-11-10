package store.controller

import store.model.ProductManagement
import store.model.PromotionManagement
import store.model.Purchase
import store.view.InputView
const val ITEM_REGEX = "^\\[\\S+-\\d+](,\\[\\S+-\\d+])*?$|^\\S+-\\d+$"


class Controller {
    private val inputView = InputView()
    private val promotionManagement = PromotionManagement()
    private val productManagement=ProductManagement(promotionManagement)

    fun start(){
        do{
            // 구매 로직
            val items = getItems()
            checkPromotion(items)



        }while(inputView.checkAdditionalPurchase()=="Y" || inputView.checkAdditionalPurchase()=="y")
    }
    fun getItems() : Purchase{
        while(true){
            try{
                val items = inputView.readItem()
                return Purchase(items,productManagement)
            }catch (e : IllegalArgumentException){
                println(e.message)
            }
        }
    }
    fun checkPromotion(items : Purchase){
        if (items.checkPromotion()){

        }

    }
    fun checkPromotionBenefit(){

    }
}