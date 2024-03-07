import controllers.AppController

fun main(args: Array<String>) {
//    val menu: MutableList<Order> = mutableListOf()
//    var jsonString = Json.encodeToString(menu)
//    var file = File("orders.json")
//    var writer = FileWriter(file)
//    writer.write(jsonString)
//    writer.close()

    val app = AppController()
    app.processApp()
}

//import ui.CreateOrderWindow
//import javax.swing.*
//
//fun main() {
//    SwingUtilities.invokeLater { CreateOrderWindow() }
//}








