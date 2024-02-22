package repository

import ObservableList
import models.User

class UserRepository(private val users: ObservableList<User>) {

    private var userId: Long = users.maxOfOrNull { u -> u.id } ?: -1
    fun getUserByUsername(username: String): User?{
        return users.firstOrNull { u -> u.login == username }
    }

    fun verifyUser(username: String, password: String): User?{
        return users.firstOrNull{ u -> u.login == username && u.password == password}
    }

    fun generateUserId(): Long{
        return ++userId
    }

    fun addNewUser(user: User){
        users.add(user)
    }

    fun saveChanges(){
        users.saveChanges()
    }
}