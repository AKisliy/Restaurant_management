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
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.concurrent.thread

/**
 * OrderController - class with main logic of dishes "preparing"
 * Uses Coroutines logic to cook dishes in different threads
 */
class OrderController(
    private val restaurant: Restaurant,
    private val menuRepository: MenuRepository
) {
    private val ordersQueue: Queue<Order> = ConcurrentLinkedQueue()
    private val screenQueue: MutableList<Order> = Collections.synchronizedList(mutableListOf())
    private val readyCallbacks: MutableMap<Long, (Long) -> Unit> = ConcurrentHashMap()
    private val jobs: MutableMap<Long, Job> = ConcurrentHashMap()

    fun makeOrder(order: Order, onReady: (Long) -> Unit ){
        ordersQueue.add(order)
        screenQueue.add(order)
        readyCallbacks[order.id] = onReady
    }

    fun processOrders() {
        // start coroutine to process orders
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                delay(1000)
                if (ordersQueue.isNotEmpty()) {
                    val order = ordersQueue.poll()
                    order.setStatus(OrderStatus.COOKING)
                    // every order has its own coroutine
                    jobs[order.id] = CoroutineScope(Dispatchers.Default).launch {
                        processOrder(order)
                    }
                }
            }
        }
    }

    private suspend fun processOrder(order: Order) {
        while(!order.isReady()){
            order.increaseTimeInKitchen()
            delay(1000)
        }
        order.setStatus(OrderStatus.READY)

        readyCallbacks[order.id]?.invoke(order.id)
    }

    fun orderPaid(order: Order){
        CoroutineScope(Dispatchers.Default).launch {
            restaurant.orderSold(order)
            jobs.remove(order.id)
            order.setStatus(OrderStatus.RECEIVED)
            for (i in 0..5) {
                delay(1000)
            }
            screenQueue.removeIf { o -> o.id == order.id }
        }
    }

    fun cancelOrder(id: Long){
        jobs[id]?.cancel()
        jobs.remove(id)
        screenQueue.removeIf { o -> o.id == id }
    }


    fun startRestaurantBoard() {
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
}