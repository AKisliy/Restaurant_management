package models

import interfaces.IUser
import kotlinx.serialization.Serializable
import java.util.*

/**
 * Admin - model of administrator
 */
@Serializable
data class Admin(
    override var id: Long,
    override var login: String,
    override var password: String
) : IUser{

}