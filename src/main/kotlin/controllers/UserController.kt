package controllers

import InputController
import OutputController
import models.Order
import models.OrderItem
import models.User
import repository.MenuRepository
import repository.OrderRepository

class UserController(
    private val outputController: OutputController,
    private val inputController: InputController,
    private val menuRepository: MenuRepository,
    private val orderController: OrderController,
    private val orderRepository: OrderRepository,
    private var user: User?
) {
    val functionsNumber = 1
    fun getFunctionsString(): String{
        return "1) MakeOrder\n"
    }

    fun setUser(user: User){
        this.user = user
    }

    fun processFunction(number: Int){
        if(number < 1 || number > functionsNumber)
            throw IllegalArgumentException("No such function for admin")
        when(number){
            1 -> makeOrder()
        }
    }

    fun makeOrder(){
        val dishes: MutableList<OrderItem> = mutableListOf()
        outputController.printMessage("Choose dishes from menu")
        while(true){
            outputController.printMessage("Enter dish name")
            var name = inputController.getUserString()
            while(menuRepository.getDishByName(name) == null){
                outputController.printMessage("No dish with this name!")
                outputController.printMessage("Want to try again?(Y/N)")
                if(inputController.getUserApproval()){
                    outputController.printMessage("Enter dish name")
                    name = inputController.getUserString()
                    continue
                }
                return
            }
            outputController.printMessage("Enter amount of $name")
            var amount = inputController.getNumber()
            while(amount < 0 || menuRepository.getAmountOfDish(name)!! < amount){
                outputController.printMessage("Amount should be positive and less than available amount")
                outputController.printMessage("Want to try again?(Y/N)")
                if(inputController.getUserApproval()){
                    outputController.printMessage("Enter amount of $name")
                    amount = inputController.getNumber()
                }
            }
            dishes.add(OrderItem(menuRepository.getDishByName(name)!!, amount))
            outputController.printMessage("Want to add more dishes?(Y/N)")
            if(inputController.getUserApproval())
                continue
            break
        }
        val order = orderRepository.create(dishes,user!!)
        orderController.makeOrder(order)
    }
}