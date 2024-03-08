package interfaces

import enums.OrderStatus

interface OrderStatusListener {
    fun onOrderStatusChanged(newStatus: OrderStatus)
}
