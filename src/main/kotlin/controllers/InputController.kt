import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDate
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

    fun getNumber(): Int{
        var choice = readln().toIntOrNull()
        while(choice == null){
            errorPrinter("Incorrect input! Try again:")
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

    fun getDate(): LocalDate{
        var input = readlnOrNull()
        var date: LocalDate
        while(true){
            if(input == null){
                errorPrinter("Incorrect input!! Try again")
                input = readlnOrNull()
                continue
            }
            try{
                date = input.toLocalDate()
                break
            }
            catch (e: IllegalArgumentException){
                errorPrinter("Can't convert your input to valid date")
                println("Try again!!")
                input = readlnOrNull()
            }
        }
        return date
    }
}