package models

import ObservableList
import kotlinx.datetime.LocalDate

import kotlinx.serialization.Serializable
import java.util.Calendar


@Serializable
class Restaurant(
    private val name: String,
    private val marginByDays: MutableMap<LocalDate, Int>
) {
    val totalMargin:Int
        get() = marginByDays.values.sumOf { x -> x }

    fun getMarginForDay(day: LocalDate): Int{
        return marginByDays.getOrDefault(day, 0)
    }

    fun orderSold(order: Order){
        val today = Calendar.getInstance()
        val ld = LocalDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
        marginByDays[ld] = marginByDays.getOrDefault(ld, 0) + order.total
    }
}