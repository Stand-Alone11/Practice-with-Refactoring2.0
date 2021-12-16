import dto.Performance
import dto.Play

class ComedyCalculator(aPerformance: Performance, aPlay: Play): PerformanceCalculator(aPerformance, aPlay) {
    override fun amount(): Int {
        var result = 30000
        if (performance.audience > 20) {
            result += 10000 + 500 * (performance.audience - 20)
        }
        result += 300 * performance.audience
        return result
    }

    override val amount = amount()
}