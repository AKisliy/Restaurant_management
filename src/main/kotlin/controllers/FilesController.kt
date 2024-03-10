import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileWriter

/**
 * FilesController - responsible for working with files
 */
class FilesController {
    /**
     * saveChanges - saves item to filePath in JSON format
     * @param item - item to save
     * @param filePath - path to the file in which item will be saved(in JSON format)
     */
    inline fun <reified T> saveChanges(item: T, filePath: String){
        val jsonString = Json.encodeToString(item)
        val file = File(filePath)
        val writer = FileWriter(file)
        writer.write(jsonString)
        writer.close()
    }

    /**
     * getData - gets object of type T from filePath
     * @param filePath - path to the file with data
     */
    inline fun <reified T> getData(filePath: String): T{
        val file = File(filePath)
        val content = file.readText()
        return Json.decodeFromString<T>(content)
    }
}