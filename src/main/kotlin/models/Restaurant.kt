package models

import FilesController
import enums.FilePaths
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialInfo

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.Transient
import java.util.Calendar
import java.util.concurrent.ConcurrentHashMap

/**
 * Restaurant - model of the restaurant
 */
@Serializable
class Restaurant(
    private val name: String,

    private val marginByDays: HashMap<LocalDate, Int>
) {
    @Transient
    private val filesController = FilesController();

    val totalMargin:Int
        get() = marginByDays.values.sumOf { x -> x }

    fun getMarginForDay(day: LocalDate): Int{
        return marginByDays.getOrDefault(day, 0)
    }

    /**
     * orderSold - function which fixes information(date and sum) about purchasing in the restaurant system
     * @param order - order, which was sold
     */
    fun orderSold(order: Order){
        val today = Calendar.getInstance()
        val ld = LocalDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
        marginByDays[ld] = marginByDays.getOrDefault(ld, 0) + order.total
        filesController.saveChanges(this, FilePaths.RESTAURANT_FILE.path)
    }
}