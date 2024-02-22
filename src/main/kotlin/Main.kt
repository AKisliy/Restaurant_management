import controllers.AppController
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Admin
import models.User
import java.io.File
import java.io.FileWriter

fun main(args: Array<String>) {
//    val users: MutableList<User> = mutableListOf()
//    val admins: MutableList<Admin> = mutableListOf()
//    var jsonString = Json.encodeToString(users)
//    var file = File("users.json")
//    var writer = FileWriter(file)
//    writer.write(jsonString)
//    writer.close()
//
//    jsonString = Json.encodeToString(admins)
//    file = File("admins.json")
//    writer = FileWriter(file)
//    writer.write(jsonString)
//    writer.close()

    val app = AppController()
    app.processApp()
}