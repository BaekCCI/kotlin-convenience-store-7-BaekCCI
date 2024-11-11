package store.controller

import store.Validator
import store.model.Receipt
import store.view.InputView

class MembershipController(private val receipt : Receipt) {
    val inputView = InputView()
    private val validator = Validator()

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
                validator.validYesOrNo(isMember)
                return isMember
            }catch(e:IllegalArgumentException){
                println(e.message)
            }
        }

    }

}