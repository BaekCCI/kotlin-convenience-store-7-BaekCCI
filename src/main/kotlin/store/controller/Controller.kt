package store.controller

import store.model.ProductManagement
import store.view.InputView
const val ITEM_REGEX = "^\\[\\S+-\\d+](,\\[\\S+-\\d+])*?$|^\\S+-\\d+$"


class Controller {
    private val inputView = InputView()
    private val productManagement=ProductManagement()

    fun start(){
        do{
            // 구매 로직
            val items = getItems()
        }while(inputView.checkAdditionalPurchase()=="Y" || inputView.checkAdditionalPurchase()=="y")
    }
    fun getItems() : Map<String, Int>{
        var items = inputView.readItem()

        //검증
        val regex = Regex(ITEM_REGEX)
        require( items.replace(" ","").matches(regex))

        //map으로 변환
        val purchaseItems : MutableMap<String,Int> = mutableMapOf()
        items = items.replace("[","").replace("]","")
        val item = items.split(",")
        item.forEach {
            var i = it.split("-")
            purchaseItems[i[0]] = i[1].toInt()
        }
        return purchaseItems
    }
}