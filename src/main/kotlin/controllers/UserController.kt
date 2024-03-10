package controllers

import OrderWindow
import enums.OrderStatus
import infrastructure.Parser
import models.User
import repository.MenuRepository
import repository.OrderRepository
import ui.CreateOrderWindow
import ui.PaymentWindow
import javax.swing.SwingUtilities

/**
 * UserController - responsible for main logic of serving the client
 */
class UserController(
    private val menuRepository: MenuRepository,
    private val orderController: OrderController,
    private val orderRepository: OrderRepository,
    private var user: User?
) {
    val functionsNumber = 1
    constructor(other: UserController) : this(other.menuRepository, other.orderController, other.orderRepository, other.user)

    fun getFunctionsString(): String{
        return "1) MakeOrder"
    }

    fun setUser(user: User){
        this.user = user
    }

    /**
     * serveClient - starts new order window for user
     */
    fun serveClient(){
        SwingUtilities.invokeLater {
            CreateOrderWindow(menuRepository.allDishes(), ::creationCallback)
        }
    }

    /**
     * creationCallback - callback function, which is called after the user created the order
     */
    private fun creationCallback(listOfDishes: List<String>){
        val dishes = Parser.parseOrder(listOfDishes, menuRepository)
        for(dish in dishes){
            menuRepository.decreaseDishAmount(dish.dish.name, dish.getAmount())
        }
        val order = orderRepository.create(dishes, user!!)
        orderController.makeOrder(order, ::orderReadyCallback)
        SwingUtilities.invokeLater {
            val window = OrderWindow(
                listOfDishes,
                menuRepository.allDishes(),
                order, orderController::cancelOrder,
                ::addingDishCallback)
            order.addListener(window)
        }
    }

    /**
     * addingDishCallback - callBack, which is called when user adds new dish on order window
     * @param orderId - id of the order, where user added new dish
     * @param dishName - name of the added dish
     * @param amount - amount of added dish
     */
    private fun addingDishCallback(orderId: Long, dishName: String, amount: Int){
        val dish = menuRepository.getDishByName(dishName) ?: throw Exception("No dish with name $dishName")
        val order = orderRepository.getOrder(orderId) ?: throw Exception("No order with this id")
        menuRepository.decreaseDishAmount(dishName, amount)
        order.addDish(dish, amount)
        orderRepository.saveChanges()
    }

    /**
     * orderReadyCallback - callback, which is called when the order is prepared
     * @param orderId - id of the order
     */
    private fun orderReadyCallback(orderId: Long){
        SwingUtilities.invokeLater{
            val order = orderRepository.getOrder(orderId) ?: throw Exception("No order with id: $orderId")
            PaymentWindow(order, ::orderPaidCallback)
        }
        orderRepository.saveChanges()
    }

    /**
     * orderPaidCallback - callback, which is called when the order is paid
     * @param orderId - id of the order
     */
    private fun orderPaidCallback(orderId: Long){
        val order = orderRepository.getOrder(orderId) ?: throw Exception("Not order with id: $orderId")
        order.setStatus(OrderStatus.RECEIVED)
        orderController.orderPaid(order)
        orderRepository.saveChanges()
    }
}