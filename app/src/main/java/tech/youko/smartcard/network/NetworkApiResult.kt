package tech.youko.smartcard.network

data class NetworkApiResult<T>(
    val status: Int = 0,
    val message: String = "",
    val data: T? = null
) {
    val isSuccessful: Boolean
        get() = status == 200
}
