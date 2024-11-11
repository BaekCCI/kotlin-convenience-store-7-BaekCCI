package store.view

import store.model.Receipt

enum class outputMessage(val message: String) {
    WELCOME_MESSAGE("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n"),
}

class OutputView {
    fun printWelcome() {
        println(outputMessage.WELCOME_MESSAGE.message)
    }

    fun printProducts(name: String, price: Int, quantity: Int, promotion: String?) {
        val formattedPrice = String.format("%,d", price)
        if(quantity == 0){
            print("- ${name} ${formattedPrice}원 재고 없음")
        }else{
            print("- ${name} ${formattedPrice}원 ${quantity}개")
        }
        promotion?.let { print(" ${promotion}") }
        println()
    }

    fun printReceipt(receipt: Receipt) { //영수증 출력
        println(String.format("%-30s","==============W 편의점==============="))
        displayPurchaseItems(receipt)
        if(receipt.extraProduct.isNotEmpty())displayPromotionItems(receipt)
        displayResult(receipt)

    }
    fun displayPurchaseItems(receipt: Receipt){
        println(String.format("%-14s %-7s %-7s","상품명","수량","금액"))
        receipt.entireProduct.forEach{(product,quantity)->
            val price = product.price*quantity
            val formattedPrice = String.format("%,d", price)
            println(String.format("%-14s %-7d %-7s",product.name,quantity,formattedPrice))
        }
    }
    fun displayPromotionItems(receipt: Receipt){
        print(String.format("%-15s","=============증"))
        println(String.format("%15s","정============="))
        receipt.extraProduct.forEach { (product,quantity)->
            println(String.format("%-14s %-7d",product.name,quantity))
        }
    }
    fun displayResult(receipt: Receipt){
        println(String.format("%30s","=".repeat(30)))
        println(String.format("%-14s %-7d %-7s","총구매액",receipt.entireProduct.size,String.format("%,d", receipt.totalPrice)))
        val discountPromotion = if(receipt.discountPromotion==0)"0" else "-${String.format("%,d", receipt.discountPromotion)}"
        println(String.format("%-14s %-7s %-7s","행사할인","",discountPromotion))
        val discountMembership = if(receipt.discountMembership==0)"0" else "-${String.format("%,d", receipt.discountMembership)}"
        println(String.format("%-14s %-7s %-7s","멤버십할인","",discountMembership))
        println(String.format("%-14s %-7s %-7s","내실돈","",String.format("%,d", receipt.resultPrice)))
    }

}