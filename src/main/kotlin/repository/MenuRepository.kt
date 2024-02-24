package repository

import ObservableList
import models.Dish
import models.MenuItem
import java.lang.IllegalArgumentException

class MenuRepository(private val menu: ObservableList<MenuItem>) {
    private var dishId: Long = menu.maxOfOrNull { d -> d.dish.id } ?: -1

    fun generateDishId(): Long{
        return ++dishId
    }

    fun getDishByName(dishName: String): Dish?{
        return menu.firstOrNull{dish -> dish.dish.name == dishName }?.dish
    }
    fun addNewDish(dish: Dish, amount: Int = 1): Boolean{
        if(menu.any{d -> d.dish.name == dish.name})
            return false
        menu.add(MenuItem(dish, amount))
        return true
    }
    fun setAmountToDish(dishName: String, amount: Int): Boolean{
        val dish = menu.firstOrNull{ dish -> dish.dish.name == dishName}
        if(dish != null){
            try {
                dish.setAmount(amount)
                return true
            }
            catch (ex: IllegalArgumentException){
                throw ex
            }
        }
        return false
    }

    fun increaseDishAmount(dishName: String, by: Int = 1): Boolean{
        val dish = menu.firstOrNull{dish -> dish.dish.name == dishName}
        if(dish != null){
            try {
                dish.increaseAmount(by)
                return true
            }
            catch (ex: IllegalArgumentException){
                throw ex
            }
        }
        return false
    }

    fun decreaseDishAmount(dishName: String, by: Int = 1): Boolean{
        val dish = menu.firstOrNull{dish -> dish.dish.name == dishName}
        if(dish != null){
            try {
                dish.decreaseAmount(by)
                return true
            }
            catch (ex: IllegalArgumentException){
                throw ex
            }
        }
        return false
    }

    fun setDishPrice(dishName: String, price: Int): Boolean{
        val dish = getDishByName(dishName)
        if(dish != null){
            try{
                dish.setPrice(price)
                return true
            }
            catch(ex: IllegalArgumentException){
                throw ex
            }
        }
        return false
    }

    fun setDishTimeForPreparing(dishName: String, time: Int): Boolean{
        val dish = getDishByName(dishName)
        if(dish != null){
            try{
                dish.setPreparingTime(time)
                return true
            }
            catch (ex: IllegalArgumentException){
                throw ex
            }
        }
        return false
    }

    fun removeDish(dishId: Long): Boolean{
        return menu.removeIf { d -> d.dish.id == dishId }
    }

    fun removeDish(dishName: String): Boolean{
        return menu.removeIf { d -> d.dish.name == dishName }
    }

    fun exists(dishName: String): Boolean{
        return menu.any { d -> d.dish.name == dishName }
    }

    fun saveChanges(){
        menu.saveChanges()
    }
}