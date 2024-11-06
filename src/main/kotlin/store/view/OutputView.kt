package store.view

enum class outputMessage(val message: String) {
    WELCOME_MESSAGE("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다."),
}


class OutputView {
    fun printWelcome(){
        println(outputMessage.WELCOME_MESSAGE)
    }
    fun printProducts(name:String, price:Int, quantity:Int, promotion:String?) {
        print("- ${name} ${price}원 ${quantity}개")
        promotion?.let{print(" ${promotion}")}
        println()
    }
    fun printReceipt(){

    }

}