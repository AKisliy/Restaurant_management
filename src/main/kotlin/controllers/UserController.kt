package controllers

import InputController
import OrderWindow
import OutputController
import infrastructure.Parser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import models.OrderItem
import models.User
import repository.MenuRepository
import repository.OrderRepository
import ui.CreateOrderWindow
import javax.swing.SwingUtilities

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
        return "1) MakeOrder"
    }

    fun setUser(user: User){
        this.user = user
    }

    fun serveClient(){
        SwingUtilities.invokeLater {
            CreateOrderWindow(menuRepository.allDishes().map { d -> d.dish.name }.toTypedArray(),
                ::creationCallback)
        }
    }

    fun creationCallback(list: List<String>){
        val dishes = Parser.parseOrder(list, menuRepository)
        val order = orderRepository.create(dishes, user!!)
        orderController.makeOrder(order)
        SwingUtilities.invokeLater {
            OrderWindow(list, menuRepository.allDishes().map { d -> d.dish.name }.toMutableList(), order.id, orderController::cancelOrder)
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