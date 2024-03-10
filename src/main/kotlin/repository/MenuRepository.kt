package repository

import infrastructure.ObservableList
import models.Dish
import models.MenuItem
import java.lang.IllegalArgumentException

/**
 * MenuRepository - class which manages data about menu items and provides appropriate methods for the rest of the app
 */
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
                menu.saveChanges()
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
                menu.saveChanges()
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
                menu.saveChanges()
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
                menu.saveChanges()
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
        val res = menu.removeIf { d -> d.dish.name == dishName }
        menu.saveChanges()
        return res
    }

    fun exists(dishName: String): Boolean{
        return menu.any { d -> d.dish.name == dishName }
    }

    fun saveChanges(){
        menu.saveChanges()
    }

    fun allDishes(): List<MenuItem>{
        return menu.getContent()
    }

    fun getAmountOfDish(name: String): Int? {
        if(getDishByName(name) == null)
            return null
        return menu.first { m -> m.dish.name == name }.getAmount()
    }
}