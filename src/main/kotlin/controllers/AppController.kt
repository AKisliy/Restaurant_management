package controllers

import FilesController
import InputController
import ObservableList
import OutputController
import enums.FilePaths
import models.Admin
import models.User
import repository.AdminRepository
import repository.UserRepository

/**
 * AppController - class which is responsible for main app's stages: start, processing and finish
 */
class AppController {
    private val filesController = FilesController()
    private lateinit var userController: UserController
    private lateinit var adminController: AdminController
    private lateinit var loginController: LoginController
    private val outputController: OutputController = OutputController()
    private val inputController: InputController = InputController(::printError)

    init{
        launchApp()
    }

    /**
     * launch - starts the app. Take all the information from JSON files and returns CinemaManager with proper lists
     */
    // TODO: implement logic of app launching
    fun launchApp(){
        val users = filesController.getData<MutableList<User>>(FilePaths.USERS_FILE.path)
        val admins = filesController.getData<MutableList<Admin>>(FilePaths.ADMINS_FILE.path)

        val oUsers = ObservableList(users)
        val oAdmins = ObservableList(admins)
        oUsers.addObserver { _, _ -> filesController.saveChanges(users, FilePaths.USERS_FILE.path) }
        oAdmins.addObserver{ _, _ -> filesController.saveChanges(admins, FilePaths.ADMINS_FILE.path)}

        val usersRepository = UserRepository(oUsers)
        val adminRepository = AdminRepository(oAdmins)
        loginController = LoginController(outputController, inputController, usersRepository, adminRepository)
    }

    /**
     * appProcess - processing the app
     */
    fun processApp(){
        outputController.printMessage("Welcome to Restaurant management system!!")
        var user = loginController.getUser()
        while(user == null){
            outputController.printMessage("Ooops, seems like something went wrong during authorization.")
            outputController.printMessage("Quit app?(Y/N)")
            if(inputController.getUserApproval())
                return;
            user = loginController.getUser()
        }
        outputController.printMessage("Now you're in system!")
        if(user is Admin){
            processAdminScenario(user)
        }
        else{
            processUserScenario(user as User)
        }
    }

    /**
     * processAdminScenario - app scenario if admin registered
     */
    private fun processAdminScenario(admin: Admin?){
        while(true){
            outputController.printMessage(adminController.getFunctionsString())

        }
    }

    /**
     * processUserScenario - app scenario if user registered
     */
    private fun processUserScenario(user: User?){

    }

    /**
     * finishApp - saves all manager information in files and finish the app
     */
    fun finishApp(){

    }

    private fun printError(message: String){
        outputController.printMessage(message)
    }
}