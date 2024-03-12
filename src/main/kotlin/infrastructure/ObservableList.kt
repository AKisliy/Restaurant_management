package infrastructure

import kotlinx.serialization.Serializable
import java.util.Observable
import java.util.function.Predicate
/**
 * ObservableList<T> - "wrapper" above the MutableList that allows you to track the addition and
 * removal of items from the list
 */
@Serializable
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

    fun getContent(): List<T> {
        return wrapped.toList();
    }
}