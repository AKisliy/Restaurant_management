package interfaces

import enums.OrderStatus

/**
 * OrderStatusListener - interface for implementing Observer pattern
 */
interface OrderStatusListener {
    fun onOrderStatusChanged(newStatus: OrderStatus)
}
