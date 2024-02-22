package models

import ObservableList
import enums.OrderStatus

data class Order(
    private val id: Long,
    var status: OrderStatus,
    var dishes: ObservableList<Dish>
)