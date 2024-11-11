package store

const val INVALID_INPUT_ERROR = "[ERROR] 잘못된 입력입니다. 다시 입력해 주세요."
class Validator {

    fun validYesOrNo(input: String) {

        require(input== "y" || input== "n"){INVALID_INPUT_ERROR}
    }
}