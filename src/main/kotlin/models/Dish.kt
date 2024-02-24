package models

import kotlinx.serialization.Serializable

@Serializable
data class Dish(
    val id: Long,
    val name: String,
    var description: String,
    var timeForPreparing: Int = 0,
    var money: Int = 0
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
