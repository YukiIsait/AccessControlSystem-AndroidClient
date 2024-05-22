package tech.youko.smartcard.entity

import java.io.Serializable

data class LoginInformation (
    val token: String,
    val information: UserInformation
): Serializable
