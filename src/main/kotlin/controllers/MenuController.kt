package controllers


import models.Dish
import repository.MenuRepository
import java.lang.IllegalArgumentException

class MenuController(
    private val menuRepo: MenuRepository
) {
    fun addDish(dish: Dish): Boolean{
        return menuRepo.addNewDish(dish)
    }

    fun removeDish(dishName: String): Boolean{
        return menuRepo.removeDish(dishName)
    }

    fun setAmountForDish(dishName: String, newAmount: Int): Boolean{
        try{
            return menuRepo.setAmountToDish(dishName, newAmount)
        }
        catch(ex: IllegalArgumentException){
            throw ex
        }
    }
}