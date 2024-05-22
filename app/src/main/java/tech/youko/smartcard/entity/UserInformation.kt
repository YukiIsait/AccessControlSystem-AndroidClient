package tech.youko.smartcard.entity

import java.io.Serializable

data class UserInformation(
    var id: String,
    var name: String,
    var authorities: String,
    var gender: String? = null,
    var phone: String? = null,
    var description: String? = null
): Serializable
