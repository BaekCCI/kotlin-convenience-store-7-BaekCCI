package store.model
import camp.nextstep.edu.missionutils.DateTimes
import java.time.LocalDate

const val PROMOTION_INFO_PATH = "src/main/resources/promotions.md"
const val FIRST_LINE = 1

data class Promotion(
    val name: String,
    val buy: Int,
    val get: Int,
    val start_date: String,
    val end_date: String
)

// 프로모션 관리 : 프로모션 적용 상품인지 확인, 프로모션 적용 수량인지 확인.
class PromotionManagement {
    private val promotionInfo: List<Promotion>

    init {
        val promotionList = java.io.File(PROMOTION_INFO_PATH).readLines().drop(FIRST_LINE)
        promotionInfo = promotionList.map { getPromotions(it) }
    }

    private fun getPromotions(promotions: String): Promotion {
        val promotion = promotions.split(",").map { it.trim() }

        return Promotion(
            name = promotion[0],
            buy = promotion[1].toInt(),
            get = promotion[2].toInt(),
            start_date = promotion[3],
            end_date = promotion[4]
        )
    }
}
