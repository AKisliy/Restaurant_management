package controllers

import enums.OrderStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import models.Order
import models.Restaurant
import java.util.*

class OrderController(
    private val restaurant: Restaurant
) {
    private val ordersQueue: Queue<Order> = LinkedList()

    fun processOrders() {
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                delay(1000)
                if (ordersQueue.isNotEmpty()) {
                    val order = ordersQueue.poll()
                    order.setStatus(OrderStatus.COOKING)
                    processOrder(order)
                }
            }
        }
    }

    private fun processOrder(order: Order) {
        order.setStatus(OrderStatus.READY)
        println("Order processed: $order")
        order.setStatus(OrderStatus.RECIEVED)
        restaurant.orderSold(order)
    }
}