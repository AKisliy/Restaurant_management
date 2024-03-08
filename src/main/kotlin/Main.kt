import controllers.AppController
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Restaurant
import java.io.File
import java.io.FileWriter

fun main(args: Array<String>) {
//    val restaurant = Restaurant("Vkusno and tochka", mutableMapOf())
//    val menu: MutableList<Order> = mutableListOf()
//    var jsonString = Json.encodeToString(restaurant)
//    var file = File("restaurant.json")
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








