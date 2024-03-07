package controllers

import InputController
import OutputController
import interfaces.IUser
import models.Admin
import models.User
import repository.AdminRepository
import repository.UserRepository

class LoginController(
    private var output: OutputController,
    private var input: InputController,
    private val usersRepo: UserRepository,
    private val adminsRepo: AdminRepository
) {
    fun getUser(): IUser?{
        var result: IUser? = null
        output.printMessage("Already registered? Login(press - 1)")
        output.printMessage("Don't have an account? Sign up(press - 2)")
        val choice = input.getNumberInRange(1, 3)
        result = when (choice) {
            2 -> registerNewUser()
            1 -> authorizeUser()
            else -> registerNewAdmin()
        }
        return result
    }

    private fun registerNewUser() : User? {
        var userName: String
        while(true) {
            output.printMessage("Input your username:")
            userName = input.getUserString()
            if (usersRepo.getUserByUsername(userName) != null) {
                output.printMessage("User with this username already exists!!")
                output.printMessage("Do you want to return to login?(Y/N)")
                if(input.getUserApproval())
                    return null;
                continue
            }
            break
        }
        output.printMessage("Create your password:")
        val password = input.getUserString()
        val user = User(usersRepo.generateUserId(), userName, password)
        usersRepo.addNewUser(user)
        usersRepo.saveChanges()
        return user
    }

    private fun registerNewAdmin(): Admin? {
        output.printMessage("Welcome to secret admin registration :)")
        var userName: String
        while(true) {
            output.printMessage("Input your admin name:")
            userName = input.getUserString()
            if (adminsRepo.getAdminByUsername(userName) != null) {
                output.printMessage("Admin with this name already exists!!")
                output.printMessage("Do you want to return to login?(Y/N)")
                if(input.getUserApproval())
                    return null;
                continue
            }
            break
        }
        output.printMessage("Create your password:")
        val password = input.getUserString()
        val user = Admin(adminsRepo.generateAdminId(), userName, password)
        adminsRepo.addNewAdmin(user)
        adminsRepo.saveChanges()
        return user
    }

    private fun authorizeUser() : IUser? {
        var username: String
        var password: String
        while(true) {
            output.printMessage("Are you User or Admin?(1 - User, 2 - Admin)")
            val choice = input.getNumberInRange(1,2)
            if(choice == 1){
                output.printMessage("Your username:")
                username = input.getUserString()
                output.printMessage("Your password:")
                password = input.getUserString()
                val user = usersRepo.verifyUser(username, password)
                if(user == null){
                    output.printMessage("No such user exists!!!")
                    output.printMessage("Want to try again?(Y/N)")
                    if(!input.getUserApproval())
                        return null
                    continue
                }
                return user
            }
            else{
                output.printMessage("Your admin login:")
                username = input.getUserString()
                output.printMessage("Your admin password:")
                password = input.getUserString()
                val admin = adminsRepo.verifyAdmin(username, password)
                if(admin == null){
                    output.printMessage("No such admin exists!!")
                    output.printMessage("Want to try again?(Y/N)")
                    if(!input.getUserApproval())
                        return null
                    continue
                }
                return admin
            }
        }
    }
}