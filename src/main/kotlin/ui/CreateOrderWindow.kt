package ui

import OrderWindow
import javax.swing.*
import java.awt.BorderLayout

class CreateOrderWindow : JFrame("Создание заказа") {
    private val dishComboBox: JComboBox<String>
    private val quantityField = JTextField("1")
    private val orderModel = DefaultListModel<String>()
    private val orderList = JList(orderModel)

    init {
        setSize(500, 400)
        layout = BorderLayout()
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        // Список доступных блюд
        val availableDishes = arrayOf("Блюдо 1", "Блюдо 2", "Блюдо 3")
        dishComboBox = JComboBox(availableDishes)

        val controlPanel = JPanel()
        controlPanel.add(dishComboBox)
        controlPanel.add(quantityField)
        val addButton = JButton("Добавить в заказ").apply {
            addActionListener { addDishToOrder() }
        }
        controlPanel.add(addButton)

        val finishOrderButton = JButton("Завершить заказ").apply {
            addActionListener {
                dispose() // Закрыть текущее окно
                SwingUtilities.invokeLater { OrderWindow(orderModel.elements().toList().toMutableList(), mutableListOf("d1")) }
            }
        }
        controlPanel.add(finishOrderButton)

        add(controlPanel, BorderLayout.NORTH)
        add(JScrollPane(orderList), BorderLayout.CENTER)

        isVisible = true
    }

    private fun addDishToOrder() {
        val selectedDish = dishComboBox.selectedItem.toString()
        val quantity = quantityField.text.toIntOrNull() ?: return JOptionPane.showMessageDialog(this, "Введите корректное количество", "Ошибка ввода", JOptionPane.ERROR_MESSAGE)
        if (quantity > 0) {
            val index = orderModel.elements().toList().indexOfFirst { o -> o.substring(0, o.indexOf('x') - 1) == selectedDish }
            if(index == -1) {
                orderModel.addElement("$selectedDish x $quantity")
            } else{
                val orderItem = orderModel[index]
                val prevQuantity = orderItem.substring(orderItem.indexOf('x') + 2).toInt()
                orderModel[index] = "$selectedDish x ${prevQuantity + quantity}"
            }
        } else {
            JOptionPane.showMessageDialog(this, "Количество должно быть больше нуля", "Ошибка ввода", JOptionPane.ERROR_MESSAGE)
        }
    }
}
