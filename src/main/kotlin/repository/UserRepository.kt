package repository

import infrastructure.ObservableList
import models.User

/**
 * UserRepository - class which manages data about users and provides appropriate methods for the rest of the app
 */
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