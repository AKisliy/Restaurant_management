package models

import interfaces.IUser
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class User(
    override var id: Long,
    override var login: String,
    override var password: String
) : IUser