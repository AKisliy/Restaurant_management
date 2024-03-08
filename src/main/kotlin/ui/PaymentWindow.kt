package ui

import models.Order
import java.awt.Dimension
import javax.swing.*
import java.awt.Font

class PaymentWindow(order: Order) : JFrame("Окно оплаты") {
    init {
        setSize(400, 600)
        layout = BoxLayout(contentPane, BoxLayout.Y_AXIS)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        val orderInfo = JLabel("Оплатите заказ №${order.id}")
        add(orderInfo)

        order.getOrderDishes().forEach { oi ->
            val dishInfo = JLabel("${oi.dish.name} x ${oi.getAmount()} - ${oi.dish.money} за шт.")
            add(dishInfo)
        }

        val totalPrice = JLabel("Итого: ${order.total}").apply {
            font = Font(font.name, Font.BOLD, font.size)
        }
        add(totalPrice)

        val cardField = JTextField("Номер карты").apply {
            preferredSize = Dimension(200, 24)
        }
        add(cardField)

        val payButton = JButton("Оплатить")
        add(payButton)

        payButton.addActionListener {
            println("Оплата произведена")
            dispose()
        }

        isVisible = true
    }
}
