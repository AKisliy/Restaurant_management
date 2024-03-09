import enums.OrderStatus
import interfaces.OrderStatusListener
import models.Order
import models.OrderItem
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.*

class OrderWindow(private val inOrder: List<String>,
                  private val availableDishes: List<String>,
                  private val order: Order,
                  private val onCancel: (Long) -> Unit,
                  private val onAddDish: (Long, String, Int) -> Unit
) : JFrame("Статус заказа"), OrderStatusListener {
    private val orderModel = DefaultListModel<String>()
    private val orderList = JList(orderModel)
    private val dishesComboBox = JComboBox(availableDishes.toTypedArray())
    private val quantityField = JTextField("1")
    private val constraints = GridBagConstraints()

    init {
        orderModel.addElement("Ваш заказ №${order.id}. Готовность можно увидеть на табло ;)")
        for(i in inOrder)
            orderModel.addElement(i)
        defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        setSize(500, 400)
        layout = GridBagLayout()

        // Список для отображения текущего заказа
        constraints.fill = GridBagConstraints.BOTH
        constraints.gridx = 0
        constraints.gridy = 0
        constraints.gridwidth = 2
        constraints.weightx = 1.0
        constraints.weighty = 0.8
        add(JScrollPane(orderList), constraints)

        // Выпадающий список доступных блюд
        constraints.fill = GridBagConstraints.HORIZONTAL
        constraints.gridx = 0
        constraints.gridy = 1
        constraints.gridwidth = 1
        constraints.weightx = 0.7
        constraints.weighty = 0.1
        add(dishesComboBox, constraints)

        // Поле для ввода количества
        constraints.fill = GridBagConstraints.HORIZONTAL
        constraints.gridx = 1
        constraints.gridy = 1
        constraints.gridwidth = 1
        constraints.weightx = 0.3
        constraints.weighty = 0.1
        add(quantityField, constraints)
        createUI()
    }

    private fun createUI() {


        // Кнопка для добавления блюда и его количества в заказ
        val addButton = JButton("Добавить в заказ")
        addButton.addActionListener {
            addDishToOrder()
        }
        constraints.fill = GridBagConstraints.HORIZONTAL
        constraints.gridx = 0
        constraints.gridy = 2
        constraints.gridwidth = 2
        constraints.weightx = 0.0
        constraints.weighty = 0.1
        add(addButton, constraints)

        // Кнопка для отмены заказа
        val cancelButton = JButton("Отменить заказ")
        cancelButton.addActionListener {
            onCancel(order.id)
            JOptionPane.showMessageDialog(this, "Заказ отменен")
            dispose() // Закрыть окно
        }
        constraints.fill = GridBagConstraints.HORIZONTAL
        constraints.gridx = 0
        constraints.gridy = 3
        constraints.gridwidth = 2
        constraints.weightx = 0.0
        constraints.weighty = 0.1
        add(cancelButton, constraints)

        isVisible = true
    }

    private fun addDishToOrder() {
        val selectedDish = dishesComboBox.selectedItem.toString()
        val quantity = quantityField.text.toIntOrNull() ?: return JOptionPane.showMessageDialog(this, "Введите корректное количество", "Ошибка ввода", JOptionPane.ERROR_MESSAGE)
        if (quantity > 0) {
            val index = orderModel.elements().toList().indexOfFirst {
                o -> o.substring(0, if (o.indexOf('x') > 0) o.indexOf('x') - 1 else 0) == selectedDish }
            if(index == -1) {
                orderModel.addElement("$selectedDish x $quantity")
            } else{
                val orderItem = orderModel[index]
                val prevQuantity = orderItem.substring(orderItem.indexOf('x') + 2).toInt()
                orderModel[index] = "$selectedDish x ${prevQuantity + quantity}"
            }
            onAddDish(order.id, selectedDish, quantity)
        } else {
            JOptionPane.showMessageDialog(this, "Количество должно быть больше нуля", "Ошибка ввода", JOptionPane.ERROR_MESSAGE)
        }
    }

    override fun onOrderStatusChanged(newStatus: OrderStatus) {
        if(newStatus == OrderStatus.READY){
            dispose()
        }
    }
}
