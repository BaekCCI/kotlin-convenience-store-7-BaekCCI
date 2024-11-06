package store.view

import camp.nextstep.edu.missionutils.Console

enum class InputMessage(val message: String) {
    PURCHASE_PRODUCT("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"),
    PROMOTION_EXTRA_ITEM("현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"),
    PROMOTION_NO_DISCOUNT("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"),
    MEMBERSHIP_DISCOUNT("멤버십 할인을 받으시겠습니까? (Y/N)"),
    ADDITIONAL_PURCHASE("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");

    fun get(name: String): String {
        return message.format(name)
    }

    fun get(name: String, noDiscountQuantity: Int): String {
        return message.format(name, noDiscountQuantity)
    }
}


class InputView {
    fun readItem(): String {
        println(InputMessage.PURCHASE_PRODUCT)
        return Console.readLine()
    }

    fun addExtraItem(name: String): String { //
        println(InputMessage.PROMOTION_EXTRA_ITEM.get(name))
        return Console.readLine()
    }

    fun confirmNoDiscount(name: String, noDiscountQuantity: Int): String {
        println(InputMessage.PROMOTION_NO_DISCOUNT.get(name, noDiscountQuantity))
        return Console.readLine()
    }

    fun checkMembershipDiscount(): String {
        println(InputMessage.MEMBERSHIP_DISCOUNT)
        return Console.readLine()
    }

    fun checkAdditionalPurchase(): String {
        println(InputMessage.ADDITIONAL_PURCHASE)
        return Console.readLine()
    }
}