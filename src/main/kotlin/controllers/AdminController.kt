package controllers

import InputController
import OutputController
import models.Dish
import repository.MenuRepository

class AdminController(
    private val outputController: OutputController,
    private val inputController: InputController,
    private val menuRepo: MenuRepository
) {
    val functionsNumber = 3
    fun getFunctionsString(): String{
        return "1) Add dish\n2) Remove dish\n3) Configure dish"
    }

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
        menuRepo.addNewDish(dish)

        while(true){
            outputController.printMessage("Dish price:")
            val price = inputController.getNumber()
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

        while(true){
            outputController.printMessage("Dish time for preparing:")
            val time = inputController.getNumber()
            try{
                dish.setPreparingTime(time)
                break
            } catch (ex: IllegalArgumentException){
                outputController.printMessage(ex.message!!)
                outputController.printMessage("Try again?(Y/N)")
                if(!inputController.getUserApproval())
                    return
            }
        }

        while(true){
            outputController.printMessage("Amount of dish:")
            val amount = inputController.getNumber()
            try{
                menuRepo.setAmountToDish(name, amount)
                break
            } catch (ex: IllegalArgumentException){
                outputController.printMessage(ex.message!!)
                outputController.printMessage("Try again?(Y/N)")
                if(!inputController.getUserApproval())
                    return
            }
        }
        menuRepo.saveChanges()
        outputController.printMessage("Successfully added")
    }

    private fun removeDish(){

    }

    fun configureDish(){

    }
}