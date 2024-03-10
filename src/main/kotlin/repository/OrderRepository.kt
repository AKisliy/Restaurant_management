package repository

import infrastructure.ObservableList
import enums.OrderStatus
import models.Order
import models.OrderItem
import models.User

/**
 * OrderRepository - class which manages data about orders and provides appropriate methods for the rest of the app
 */
class OrderRepository(private val orders: ObservableList<Order>) {

    private var orderId: Long = orders.maxOfOrNull { u -> u.id } ?: -1
    fun getOrder(id: Long): Order?{
        return orders.firstOrNull { o -> o.id == id }
    }

    fun generateOrderId(): Long{
        return ++orderId
    }

    fun create(dishes: MutableList<OrderItem>, user: User): Order {
        val order = Order(generateOrderId(),user.id,OrderStatus.ACCEPTED, ObservableList(dishes))
        addOrder(order)
        return order
    }

    private fun addOrder(order: Order){
        orders.add(order)
        orders.saveChanges()
    }

    fun saveChanges(){
        orders.saveChanges()
    }
}