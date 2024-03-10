package models

import kotlinx.serialization.Serializable

/**
 * MenuItem - represents dish in menu
 */
@Serializable
class MenuItem(
    val dish: Dish,
    private var amount: Int
) {

    fun getAmount(): Int{
        return amount
    }

    fun setAmount(newAmount: Int){
        if(newAmount < 0)
            throw IllegalArgumentException("Amount can't be negative")
        amount = newAmount
    }
    fun increaseAmount(by: Int = 1){
        if(by < 0)
            throw IllegalArgumentException("Can't increase by negative number")
        amount += by;
    }
    fun decreaseAmount(by: Int = 1){
        if(by < 0)
            throw IllegalArgumentException("Can't decrease by negative number")
        amount -= by
    }

    override fun toString(): String {
        return "${dish.name}: ${dish.money}$"
    }
}