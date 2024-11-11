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
    private fun displayPurchaseItems(receipt: Receipt){
        println(String.format("%-14s %-7s %-7s","상품명","수량","금액"))
        receipt.entireProduct.forEach{(product,quantity)->
            val price = product.price*quantity
            val formattedPrice = String.format("%,d", price)
            println(String.format("%-14s %-7d %-7s",product.name,quantity,formattedPrice))
        }
    }
    private fun displayPromotionItems(receipt: Receipt){
        print(String.format("%-15s","=============증"))
        println(String.format("%15s","정============="))
        receipt.extraProduct.forEach { (product,quantity)->
            println(String.format("%-14s %-7d",product.name,quantity))
        }
    }
    private fun displayResult(receipt: Receipt){
        println(String.format("%30s","=".repeat(35)))
        println(String.format("%-14s %-8d %8s","총구매액",receipt.getTotalCount(),String.format("%,d", receipt.totalPrice)))
        println(String.format("%-14s %-8s %8s","행사할인","","-${String.format("%,d", receipt.discountPromotion)}"))
        println(String.format("%-14s %-8s %8s","멤버십할인","", "-${String.format("%,d", receipt.discountMembership)}"))
        println(String.format("%-14s %-8s %8s","내실돈","",String.format("%,d", receipt.resultPrice)))
        println()
    }

}