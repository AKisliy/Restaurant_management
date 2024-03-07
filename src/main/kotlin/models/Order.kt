package models

import ObservableList
import enums.OrderStatus
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.text.StringBuilder

@Serializable
data class Order(
    val id: Long,
    val userId: Long,
    private var status: OrderStatus,
    private var dishes: ObservableList<OrderItem> = ObservableList(mutableListOf())
){
    private var inKitchen = 0

    val total: Int
        get() = dishes.sumOf { d -> d.dish.money * d.getAmount() }

    fun setStatus(status: OrderStatus){
        this.status = status
    }

    fun increaseTimeInKitchen(by: Int = 1){
        inKitchen += by
    }

    fun isReady(): Boolean{
        return totalTime == 0
    }

    fun addDish(dish: Dish, amount: Int = 1){
        val orderDish = dishes.firstOrNull{d -> d.dish.name == dish.name}
        if(orderDish != null)
            orderDish.increaseAmount(amount)
        else
            dishes.add(OrderItem(dish,amount))
    }

    val totalTime: Int
        get() = dishes.sumOf { d -> d.dish.timeForPreparing * d.getAmount() } - inKitchen

    override fun toString(): String {
        return "$userId   ${status.name}   ~${totalTime} min"
    }

}