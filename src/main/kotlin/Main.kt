import controllers.AppController
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Restaurant
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.lang.Exception

fun main() {
    try {
        val app = AppController()
        app.processApp()
    } catch (ex: IOException){
        println("Probably one of the files is missing.")
        println("Please check the paths in enums/FilePaths")
    } catch(ex: Exception) {
        println("Unknown exception!")
        println(ex.message)
    }
}








