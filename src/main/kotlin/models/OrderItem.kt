package models

import kotlinx.serialization.Serializable

/**
 * OrderItem - model of dish in the order
 */
@Serializable
data class OrderItem(
    val dish: Dish,
    private var amount: Int
){
    fun getAmount(): Int{
        return amount
    }
    fun increaseAmount(by: Int = 1){
        if(by < 0)
            return
        amount += by;
    }
    fun decreaseAmount(by: Int = 1){
        if(by < 0)
            return
        amount -= by
    }
}