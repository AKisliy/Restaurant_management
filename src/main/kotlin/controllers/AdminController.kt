package controllers

import InputController
import OutputController
import models.Dish
import repository.MenuRepository

// TODO - complete dish editing function + logic if we're out of dish
class AdminController(
    private val outputController: OutputController,
    private val inputController: InputController,
    private val menuRepo: MenuRepository
) {
    private val editingOptions: List<String> = listOf("Set amount", "Set price", "Set time for preparing")
    val adminFunctions: List<String> = listOf("Add dish", "Remove dish", "Configure dish")
    val functionsNumber = 3

    fun processFunction(number: Int){
        if(number < 1 || number > functionsNumber)
            throw IllegalArgumentException("No such function for admin")
        when(number){
            1 -> addDish()
            2 -> removeDish()
            3 -> configureDish()
        }
    }
    private fun addDish(){
        outputController.printMessage("Input information about new dish:")
        outputController.printMessage("Dish name:")
        var name = inputController.getUserString()
        while(menuRepo.exists(name)){
            outputController.printMessage("Dish with this name already exists!!")
            outputController.printMessage("Try again?(Y/N)")
            if(!inputController.getUserApproval())
                return
            outputController.printMessage("Dish name")
            name = inputController.getUserString()
        }
        outputController.printMessage("Short description for dish")
        val description = inputController.getUserString()
        val dish = Dish(menuRepo.generateDishId(), name, description)

        var price: Int
        while(true){
            outputController.printMessage("Dish price:")
            price = inputController.getNumber()
            try{
                dish.setPrice(price)
                break
            } catch (ex: IllegalArgumentException){
                outputController.printMessage(ex.message!!)
                outputController.printMessage("Try again?(Y/N)")
                if(!inputController.getUserApproval())
                    return
            }
        }

        val time = getTimeForPreparing(dish) ?: return

        val amount = getDishAmount() ?: return

        menuRepo.addNewDish(dish)
        menuRepo.setAmountToDish(dish.name, amount)
        menuRepo.saveChanges()
        outputController.printMessage("Successfully added")
    }

    private fun removeDish(){
        outputController.printMessage("Dish name for removing:")
        var name = inputController.getUserString()
        while(!menuRepo.exists(name)){
            outputController.printMessage("No dish with this name in menu!!")
            outputController.printMessage("Try again?(Y/N)")
            if(!inputController.getUserApproval())
                return
            outputController.printMessage("Dish name:")
            name = inputController.getUserString()
        }
        menuRepo.removeDish(name)
        menuRepo.saveChanges()
        outputController.printMessage("Successfully removed")
    }

    private fun configureDish(){
        outputController.printMessage("Dish name for configuring:")
        var name = inputController.getUserString()
        while(!menuRepo.exists(name)){
            outputController.printMessage("No dish with this name in menu!!")
            outputController.printMessage("Try again?(Y/N)")
            if(!inputController.getUserApproval())
                return
            outputController.printMessage("Dish name:")
            name = inputController.getUserString()
        }
        outputController.printMessage("Choose option:")
        outputController.printNumberedList(editingOptions)
        val choice = inputController.getNumberInRange(1, editingOptions.size)
    }

    private fun setAmount(dishName: String){
        outputController.printMessage("Enter new amount of $dishName")
        var amount = inputController.getNumber()

    }

    private fun getDishAmount() : Int? {
        var amount: Int
        while(true){
            outputController.printMessage("Amount of dish:")
            amount = inputController.getNumber()
            if(amount < 0) {
                outputController.printMessage("Amount can't be negative!")
                outputController.printMessage("Try again?(Y/N)")
                if(!inputController.getUserApproval())
                    return null
                continue
            }
            break
        }
        return amount
    }

    private fun getTimeForPreparing(dish: Dish) : Int? {
        var time: Int
        while(true){
            outputController.printMessage("Dish time for preparing:")
            time = inputController.getNumber()
            try{
                dish.setPreparingTime(time)
                break
            } catch (ex: IllegalArgumentException){
                outputController.printMessage(ex.message!!)
                outputController.printMessage("Try again?(Y/N)")
                if(!inputController.getUserApproval())
                    return null
            }
        }
        return time
    }
}