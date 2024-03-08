package controllers

import InputController
import OrderWindow
import OutputController
import infrastructure.Parser
import jdk.jfr.DataAmount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import models.OrderItem
import models.User
import repository.MenuRepository
import repository.OrderRepository
import ui.CreateOrderWindow
import ui.PaymentWindow
import javax.swing.SwingUtilities

// TODO - обработать оплату заказа в окне(нужно менять статус на recieved и убирать с общего табло)
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

    private fun creationCallback(list: List<String>){
        val dishes = Parser.parseOrder(list, menuRepository)
        val order = orderRepository.create(dishes, user!!)
        orderController.makeOrder(order, ::orderReadyCallback)
        SwingUtilities.invokeLater {
            val window = OrderWindow(list,
                menuRepository.allDishes().map { d -> d.dish.name }.toMutableList(),
                order, orderController::cancelOrder,
                ::addingDishCallback)
            order.addListener(window)
        }
    }

    private fun addingDishCallback(orderId: Long, dishName: String, amount: Int){
        val dish = menuRepository.getDishByName(dishName) ?: throw Exception("No dish with name $dishName")
        val order = orderRepository.getOrder(orderId) ?: throw Exception("No order with this id")
        order.addDish(dish, amount)
    }

    private fun orderReadyCallback(orderId: Long){
        SwingUtilities.invokeLater{
            val order = orderRepository.getOrder(orderId) ?: throw Exception("No order with id: $orderId")
            PaymentWindow(order)
        }
    }
}