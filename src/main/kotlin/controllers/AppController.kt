package controllers

import FilesController
import ObservableList
import enums.FilePaths
import models.Admin
import models.User

/**
 * AppController - class which is responsible for main app's stages: start, processing and finish
 */
class AppController {
    private val filesController = FilesController()
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
    }
    /**
     * appProcess - processing the app
     */
    fun appProcess(cinemaManager: CinemaManager){
        var choice = cinemaManager.interactor.getMenuChoice()
        while(true){
            when(choice){
                1-> cinemaManager.sellTicket()
                2-> cinemaManager.refundTicket()
                3-> cinemaManager.displaySeatStatus()
                4-> cinemaManager.editMovieData()
                5-> cinemaManager.editSessionData()
                6-> return
            }
            if(cinemaManager.interactor.askForApproval("Return to menu?"))
            {
                choice = cinemaManager.interactor.getMenuChoice()
                continue
            }
            break
        }
    }
    /**
     * finishApp - saves all manager information in files and finish the app
     */
    fun finishApp(manager: CinemaManager){
        manager.sessions.saveChanges()
        manager.cinemaHall.notifyObservers()
        manager.tickets.saveChanges()
        manager.movies.saveChanges()
        manager.interactor.printWithColor("Thank you for using!", Colors.BLUE)
    }
}