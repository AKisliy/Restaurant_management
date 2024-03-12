import kotlinx.serialization.descriptors.StructureKind

/**
 * OutputController - class which is responsible for printing information on console
 */
class OutputController {
    fun printMessage(message: String){
        println(message)
    }

    fun <T> printNumberedList(list: List<T>){
        for(i in list.indices){
            println("${i + 1}) ${list[i]}")
        }
    }
}