package models

import ObservableList
import enums.OrderStatus
import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val id: Long,
    val userId: Long,
    private var status: OrderStatus,
    private var dishes: ObservableList<OrderItem> = ObservableList(mutableListOf())
){
    val total: Int
        get() = dishes.sumOf { d -> d.dish.money * d.getAmount() }

    fun setStatus(status: OrderStatus){
        this.status = status
    }

    fun addDish(dish: Dish){
        val orderDish = dishes.firstOrNull{d -> d.dish.name == dish.name}
        if(orderDish != null)
            orderDish.increaseAmount()
        else
            dishes.add(OrderItem(dish,1))
    }

}