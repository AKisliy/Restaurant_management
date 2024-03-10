package repository

import infrastructure.ObservableList
import models.Admin

/**
 * AdminRepository - class which manages data about admins and provides appropriate methods for the rest of the app
 */
class AdminRepository(private val admins: ObservableList<Admin>) {

    private var adminId: Long = admins.maxOfOrNull { u -> u.id } ?: -1
    fun getAdminByUsername(username: String): Admin?{
        return admins.firstOrNull { a -> a.login == username }
    }

    fun verifyAdmin(username: String, password: String): Admin?{
        return admins.firstOrNull{ a -> a.login == username && a.password == password}
    }

    fun generateAdminId(): Long{
        return ++adminId
    }

    fun addNewAdmin(admin: Admin){
        admins.add(admin)
    }

    fun saveChanges(){
        admins.saveChanges()
    }
}