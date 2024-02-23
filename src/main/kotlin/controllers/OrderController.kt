package controllers

import com.googlecode.lanterna.TerminalPosition
import enums.OrderStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import models.Order
import models.Restaurant
import java.util.*
import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.gui2.*
import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import kotlin.concurrent.thread

class OrderController(
    private val restaurant: Restaurant
) {
    private val ordersQueue: Queue<Order> = LinkedList()

    fun processOrders() {
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                delay(1000)
                if (ordersQueue.isNotEmpty()) {
                    val order = ordersQueue.poll()
                    order.setStatus(OrderStatus.COOKING)
                    processOrder(order)
                }
            }
        }
    }

    private fun processOrder(order: Order) {
        order.setStatus(OrderStatus.READY)
        println("Order processed: $order")
        order.setStatus(OrderStatus.RECIEVED)
        restaurant.orderSold(order)
    }


    fun testScreen() {
        val orderQueue = LinkedList<String>()
        orderQueue.add("bebra")
        orderQueue.add("Order 2")
        orderQueue.add("Order 3")

        val terminalFactory = DefaultTerminalFactory()
        val terminal: Screen = TerminalScreen(terminalFactory.createTerminal())

        val gui: WindowBasedTextGUI = MultiWindowTextGUI(terminal)


        val orderWindow = BasicWindow("Orders Queue")
        val orderPanel = Panel(BorderLayout())

        val orderTextBox = TextBox(TerminalSize(1, 10), TextBox.Style.MULTI_LINE)


        orderPanel.addComponent(orderTextBox)
        orderWindow.component = orderPanel

        val menuWindow = BasicWindow("Menu")
        val menuPanel = Panel(BorderLayout())
        val menuTextBox = TextBox(TerminalSize(1,10), TextBox.Style.MULTI_LINE)

        menuPanel.addComponent(menuTextBox)
        menuWindow.component = menuTextBox

        val terminalSize = terminal.terminalSize

        // Вычисляем положение окон относительно терминала
        val orderWindowPosition = TerminalPosition.TOP_LEFT_CORNER
        val menuWindowPosition = TerminalPosition(terminalSize.columns/2, 0)

        // Устанавливаем положение окон
        orderWindow.setPosition(orderWindowPosition)
        menuWindow.setPosition(menuWindowPosition)

        gui.addWindow(orderWindow)
        gui.addWindow(menuWindow)

        terminal.startScreen()

        thread {
            while (true) {
                val ordersCopy = LinkedList(orderQueue)
                orderTextBox.text = ""
                ordersCopy.forEach { orderTextBox.addLine(it) }
                // remove first blank line
                if (orderTextBox.lineCount > 0 && orderTextBox.getLine(0).isBlank()) {
                    orderTextBox.removeLine(0)
                }
//                panel.invalidate()
//                terminal.refresh()
                terminal.doResizeIfNecessary()
                gui.updateScreen()
                // Ждем некоторое время перед следующим обновлением
                Thread.sleep(1000)
            }
        }

        thread {
            gui.waitForWindowToClose(orderWindow)
            terminal.stopScreen()
        }
    }

    // Функция для форматирования текста заказов для отображения в окне
    fun getOrderText(orderQueue: Queue<String>): String {
        val sb = StringBuilder()
        for (order in orderQueue) {
            sb.append(order).append("\n")
        }
        return sb.toString()
    }

}