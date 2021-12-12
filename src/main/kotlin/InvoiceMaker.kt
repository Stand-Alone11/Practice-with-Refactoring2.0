import dto.Performance
import dto.Play
import java.text.NumberFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.floor

class InvoiceMaker {
    fun statement(invoice: HashMap<String, Any>, plays: HashMap<String, Play>): String {
        fun playFor(aPerformance: Performance): Play {
            return plays[aPerformance.playId]!!
        }

        fun amountFor(aPerformance: Performance): Int {
            var result = 0 // 명확한 이름으로 변경
            when(playFor(aPerformance).type) { // playFor() 함수 인라인
                "tragedy" -> {
                    result = 40000;
                    if(aPerformance.audience > 30) {
                        result += 1000 * (aPerformance.audience - 30)
                    }
                }
                "comedy" -> {
                    result = 30000
                    if(aPerformance.audience > 20) {
                        result += 10000 + 500 * (aPerformance.audience - 20)
                    }
                    result += 300 * aPerformance.audience
                }
                else -> throw Exception("알 수 없는 장르: ${playFor(aPerformance).type}")
            }
            return result
        }

        fun volumeCreditsFor(aPerformance: Performance): Int {
            var result = 0
            result += Math.max(aPerformance.audience - 30 , 0)
            if("comedy" == playFor(aPerformance).type) result += floor((aPerformance.audience / 5).toDouble()).toInt()
            return result
        }

        fun usd(aNumber: Double): String {
            return NumberFormat.getCurrencyInstance().apply {
                maximumFractionDigits = 2
                minimumFractionDigits = 2
                currency = Currency.getInstance(Locale.US)
            }.format(aNumber / 100)
        }

        var totalAmount = 0
        var result = "청구 내역 (고객명: ${invoice["customer"]})\n"

        for(perf in invoice["performances"] as List<Performance>) {
            // print invoices
            result += " ${playFor(perf).name}: ${usd(amountFor(perf).toDouble())} (${perf.audience}석)\n" // playFor(), amountFor() 함수 인라인
            totalAmount += amountFor(perf)
        }

        var volumeCredits = 0 // 문장 슬라이드를 적용하여 변수 선언, 초기화를 반복문 앞으로 이동
        for(perf in invoice["performances"] as List<Performance>) {
            volumeCredits += volumeCreditsFor(perf)
        }

        result += "총액: ${usd(totalAmount.toDouble())}\n"
        result += "적립 포인트: ${volumeCredits}점\n"
        return result
    }
}