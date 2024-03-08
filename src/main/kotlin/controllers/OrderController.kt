package controllers

import com.googlecode.lanterna.TerminalPosition
import enums.OrderStatus
import models.Order
import models.Restaurant
import java.util.*
import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*
import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import kotlinx.coroutines.*
import repository.MenuRepository
import kotlin.collections.HashMap
import kotlin.concurrent.thread

class OrderController(
    private val restaurant: Restaurant,
    private val menuRepository: MenuRepository
) {
    private val ordersQueue: Queue<Order> = LinkedList()
    private val screenQueue: MutableList<Order> = mutableListOf()
    private val jobs: MutableMap<Long, Job> = HashMap()

    fun makeOrder(order: Order){
        ordersQueue.add(order)
        screenQueue.add(order)
    }

    fun processOrders() {
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                delay(1000)
                if (ordersQueue.isNotEmpty()) {
                    val order = ordersQueue.poll()
                    order.setStatus(OrderStatus.COOKING)
                    jobs[order.id] = CoroutineScope(Dispatchers.Default).launch {
                        processOrder(order)
                    }
                }
            }
        }
    }

    private suspend fun processOrder(order: Order) {
        for(i in 0..<order.totalTime){
            order.increaseTimeInKitchen()
            delay(1000)
        }
        order.setStatus(OrderStatus.READY)
        //println("Order processed: $order")
        //order.setStatus(OrderStatus.RECIEVED)
        restaurant.orderSold(order)
        jobs.remove(order.id)
    }

    fun cancelOrder(id: Long){
        jobs[id]?.cancel()
        jobs.remove(id)
        screenQueue.removeIf { o -> o.id == id }
    }


    fun startScreen() {
        val terminalFactory = DefaultTerminalFactory()
        val terminal: Screen = TerminalScreen(terminalFactory.createTerminal())

        val gui: WindowBasedTextGUI = MultiWindowTextGUI(terminal)

        val orderWindow = BasicWindow("         Orders Queue         ")
        val orderPanel = Panel(BorderLayout())

        val orderTextBox = TextBox(TerminalSize(1, 10), TextBox.Style.MULTI_LINE)

        orderPanel.addComponent(orderTextBox)
        orderWindow.component = orderPanel

        val menuWindow = BasicWindow("Restaurant Menu")
        val menuPanel = Panel(BorderLayout())
        val menuTextBox = TextBox(TerminalSize(1,10), TextBox.Style.MULTI_LINE)

        menuPanel.addComponent(menuTextBox)
        menuWindow.component = menuPanel

        val terminalSize = terminal.terminalSize

        val orderWindowPosition = TerminalPosition.TOP_LEFT_CORNER
        val menuWindowPosition = TerminalPosition(terminalSize.columns/2, 0)

        gui.addWindow(orderWindow)
        gui.addWindow(menuWindow)

        orderWindow.position = orderWindowPosition
        menuWindow.position = menuWindowPosition

        terminal.startScreen()

        thread {
            while (true) {
                val ordersCopy = LinkedList(screenQueue)
                orderTextBox.text = ""
                ordersCopy.forEach { orderTextBox.addLine(it.toString()) }
                // remove first blank line
                if (orderTextBox.lineCount > 1 && orderTextBox.getLine(0).isBlank()) {
                    orderTextBox.removeLine(0)
                }

                terminal.doResizeIfNecessary()
                gui.updateScreen()
                Thread.sleep(1000)
            }
        }

        thread {
            while(true){
                val items = menuRepository.allDishes()
                menuTextBox.text = ""
                items.forEach { menuTextBox.addLine(it.toString()) }

                if(menuTextBox.lineCount > 1 && menuTextBox.getLine(0).isBlank()){
                    menuTextBox.removeLine(0)
                }

                terminal.doResizeIfNecessary()
                gui.updateScreen()
                Thread.sleep(5000)
            }
        }

        thread {
            gui.waitForWindowToClose(orderWindow)
            terminal.stopScreen()
        }
    }

    fun getOrderText(orderQueue: Queue<String>): String {
        val sb = StringBuilder()
        for (order in orderQueue) {
            sb.append(order).append("\n")
        }
        return sb.toString()
    }

}