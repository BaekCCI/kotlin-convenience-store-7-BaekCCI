package store.model
const val ITEM_REGEX = "^\\[\\S+-\\d+](,\\[\\S+-\\d+])*?$|^\\S+-\\d+$"

class Purchase (val items : String) {

    init{
        require(validateItems()){"[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."}
    }
    fun validateItems() : Boolean{
        val regex = Regex(ITEM_REGEX)
        val normalizedItems = items.replace(" ", "")
        if(normalizedItems.matches(regex)) return true
        return false
    }
}