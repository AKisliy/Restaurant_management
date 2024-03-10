package ui

import models.MenuItem
import javax.swing.*
import java.awt.BorderLayout

/**
 * CreateOrderWindow - window, which user interacts with while making order
 */
class CreateOrderWindow(
    private val availableDishes: List<MenuItem>,
    private val callBack: (List<String>) -> Unit) : JFrame("Создание заказа") {
    private val dishComboBox: JComboBox<String>
    private val quantityField = JTextField("1")
    private val orderModel = DefaultListModel<String>()
    private val orderList = JList(orderModel)

    init {
        setSize(500, 400)
        layout = BorderLayout()
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        dishComboBox = JComboBox(availableDishes.map { d -> d.dish.name }.toTypedArray())

        val controlPanel = JPanel()
        controlPanel.add(dishComboBox)
        controlPanel.add(quantityField)
        val addButton = JButton("Добавить в заказ").apply {
            addActionListener { addDishToOrder() }
        }
        controlPanel.add(addButton)

        val finishOrderButton = JButton("Сделать заказ").apply {
            addActionListener {
                dispose() // Закрыть текущее окно
                callBack(orderModel.elements().toList())
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
            val dish = availableDishes.first { x -> x.dish.name == selectedDish }
            if(quantity > dish.getAmount()){
                val amount = dish.getAmount()
                if(amount == 0){
                    JOptionPane.showMessageDialog(this, "Извините, блюдо ${dish.dish.name} закончилось сейчас :(", "Недоступно", JOptionPane.ERROR_MESSAGE)
                    return
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Извините, такого количества ${dish.dish.name} у нас нет :( Есть только ${dish.getAmount()} шт",
                        "Слишком большое количество",
                        JOptionPane.ERROR_MESSAGE
                    )
                    return
                }
            }
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
