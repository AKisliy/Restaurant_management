package models

import kotlinx.serialization.Serializable

@Serializable
data class Dish(
    val id: Long,
    val name: String,
    var timeForPreparing: Int,
    var description: String,
    var money: Int
){
    fun setPrice(price: Int){
        if(price < 0)
            throw IllegalArgumentException("Price can't be negative")
        money = price
    }

    fun setPreparingTime(time: Int){
        if(time < 0)
            throw IllegalArgumentException("Time for preparing can't be negative")
        timeForPreparing = time
    }
}
