package models

import kotlinx.serialization.Serializable

@Serializable
data class Dish(
    private val id: Long,
    var name: String,
    var timeForPreparing: Int,
    var description: String,
    var money: Int
)
