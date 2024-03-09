package ui

import models.Order
import java.awt.Dimension
import javax.swing.*
import java.awt.Font
import kotlin.reflect.KSuspendFunction1

class PaymentWindow(order: Order, callback: (Long) -> Unit) : JFrame("Окно оплаты") {
    private var orderInfo = JLabel()
    private var totalPrice = JLabel()
    private var cardField = JTextField()
    init {
        setSize(400, 600)
        layout = BoxLayout(contentPane, BoxLayout.Y_AXIS)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        orderInfo = JLabel("Оплатите заказ №${order.id}")
        add(orderInfo)

        order.getOrderDishes().forEach { oi ->
            val dishInfo = JLabel("${oi.dish.name} x ${oi.getAmount()} - ${oi.dish.money}$ за шт.")
            add(dishInfo)
        }

        totalPrice = JLabel("Итого: ${order.total}$").apply {
            font = Font(font.name, Font.BOLD, font.size)
        }
        add(totalPrice)

        cardField = JTextField("Номер карты").apply {
            preferredSize = Dimension(200, 24)
        }
        add(cardField)

        val payButton = JButton("Оплатить")
        add(payButton)

        payButton.addActionListener {
            if(!processPayment()){
                cardField.text = ""
            }
            else{
                callback(order.id)
                dispose()
            }
        }
        pack()
        isVisible = true
    }

    private fun processPayment(): Boolean{
        val card = cardField.text.toIntOrNull()
        if(card == null || card < 0) {
            JOptionPane.showMessageDialog(
                this,
                "Введите корректный номер карты",
                "Ошибка ввода",
                JOptionPane.ERROR_MESSAGE
            )
            return false
        }
        JOptionPane.showMessageDialog(
            this,
            "Оплата произведена! Спасибо за заказ :)",
            "Успешно",
            JOptionPane.INFORMATION_MESSAGE)
        return true
    }
}
