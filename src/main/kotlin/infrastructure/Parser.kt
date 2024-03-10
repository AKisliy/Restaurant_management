package infrastructure

import models.OrderItem
import repository.MenuRepository

/**
 * Parser - object, which is responsible for parsing data from user's order to models
 */
object Parser {
    fun parseOrder(items: List<String>, menuRepository: MenuRepository): MutableList<OrderItem> {
        val dishes: MutableList<OrderItem> = mutableListOf()
        for(item in items){
            val delimiterIndex = item.indexOf('x')
            val dishName = item.substring(0, delimiterIndex - 1)
            val dishCount = item.substring(delimiterIndex + 2).toInt()
            val dish = menuRepository.getDishByName(dishName)
            dishes.add(OrderItem(dish!!, dishCount))
        }
        return dishes
    }
}