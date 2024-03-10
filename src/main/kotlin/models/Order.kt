package models

import infrastructure.ObservableList
import enums.OrderStatus
import interfaces.OrderStatusListener
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Order - model of order, which consists of OrderItems
 */
@Serializable
data class Order(
    val id: Long,
    val userId: Long,
    private var status: OrderStatus,
    private var dishes: ObservableList<OrderItem> = ObservableList(mutableListOf())
){
    @Transient
    private val listeners: MutableList<OrderStatusListener> = mutableListOf()
    @Transient
    private var inKitchen = 0

    val total: Int
        get() = dishes.sumOf { d -> d.dish.money * d.getAmount() }

    fun setStatus(status: OrderStatus){
        this.status = status
        listeners.forEach{ l -> l.onOrderStatusChanged(status) }
        dishes.saveChanges()
    }

    fun getOrderDishes(): List<OrderItem>{
        return dishes.getContent()
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

    fun addListener(orderStatusListener: OrderStatusListener){
        listeners.add(orderStatusListener)
    }

    val totalTime: Int
        get() = dishes.sumOf { d -> d.dish.timeForPreparing * d.getAmount() } - inKitchen

    override fun toString(): String {
        return "$id   ${status.name}   ~${totalTime} min"
    }

}