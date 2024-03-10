package interfaces

/**
 * IUser - interface, which is common for all users of the app
 */
interface IUser {
    var id: Long
    var login: String
    var password: String
}