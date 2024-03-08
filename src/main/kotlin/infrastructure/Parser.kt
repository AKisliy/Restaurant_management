package infrastructure

import models.OrderItem
import repository.MenuRepository

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