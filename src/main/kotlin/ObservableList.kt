import java.util.Observable
import java.util.function.Predicate
/**
 * ObservableList<T> - "обертка" над MutableList, которая позволяет отслеживать добавление и
 * удаление элементов из листа
 */
class ObservableList<T>(private val wrapped: MutableList<T>): MutableList<T> by wrapped, Observable() {
    fun saveChanges(){
        setChanged()
        notifyObservers()
    }

    override fun add(element: T): Boolean {
        if (wrapped.add(element)) {
            setChanged()
            notifyObservers()
            return true
        }
        return false
    }

    override fun removeIf(filter: Predicate<in T>): Boolean {
        if(wrapped.removeIf(filter)){
            setChanged()
            notifyObservers()
            return true
        }
        return false
    }
}