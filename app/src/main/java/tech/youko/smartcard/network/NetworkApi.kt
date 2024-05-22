package tech.youko.smartcard.network

import retrofit2.http.GET
import retrofit2.http.Query
import tech.youko.smartcard.entity.LoginInformation

interface NetworkApi {
    @GET("/management/authorization/login")
    suspend fun login(
        @Query("id") id: String,
        @Query("password") password: String
    ): NetworkApiResult<LoginInformation>

    @GET("/management/authorization/validate")
    suspend fun validate(
        @Query("token") id: String
    ): NetworkApiResult<Boolean>
}
