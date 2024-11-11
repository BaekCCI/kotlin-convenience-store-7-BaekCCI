package store.controller

import store.model.Receipt
import store.view.InputView

class MembershipController(private val receipt : Receipt) {
    val inputView = InputView()

    fun startMembership(){
        val isMember = checkIsMember()
        if(isMember=="y"){
            receipt.setDiscountMembership()
        }
        receipt.setResultPrice()
    }
    fun checkIsMember() : String{
        while(true){
            try {
                val isMember = inputView.checkMembershipDiscount().lowercase()
                require(validYesOrNo(isMember)){"[ERROR] 잘못된 입력입니다. 다시 입력해 주세요."}
                return isMember
            }catch(e:IllegalArgumentException){
                println(e.message)
            }
        }

    }
    private fun validYesOrNo(input: String): Boolean {
        return input == "y" || input == "n"
    }
}