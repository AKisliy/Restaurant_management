import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.LocalDateTime
import java.lang.IllegalArgumentException
/**
 * InputController - class which is responsible for getting user input from console
 */
class InputController(
    private val errorPrinter: (String) -> Unit
) {
    fun getNumberInRange(rangeStart: Int, rangeEnd: Int): Int{
        var choice = readln().toIntOrNull()
        while(choice == null || choice < rangeStart || choice > rangeEnd){
            errorPrinter("Wrong input! Try again:")
            choice = readln().toIntOrNull()
        }
        return choice
    }

    fun getUserString(): String{
        var input = readlnOrNull()
        while(input == null){
            errorPrinter("It's not a valid input!")
            input = readlnOrNull()
        }
        return input
    }

    fun getUserApproval(): Boolean{
        var input = readlnOrNull()
        while(input == null || (input[0] != 'Y' && input[0] != 'N')){
            errorPrinter("Unknown command. Try again!")
            input = readlnOrNull()
        }
        return input[0] == 'Y'
    }
}