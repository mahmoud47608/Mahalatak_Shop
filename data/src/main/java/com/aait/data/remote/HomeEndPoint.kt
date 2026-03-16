package com.aait.data.remote

import com.aait.domain.entity.base.AnyResponse
import com.aait.domain.entity.AuthData
import com.aait.domain.entity.base.BaseResponse
import com.aait.domain.entity.general.CountriesResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface HomeEndPoint {

    @FormUrlEncoded
    @POST("sign-in")
    suspend fun login(
        @Field("country_code") countryCode: String,
        @Field("phone") phone: String,
        @Field("password") password: String,
        @Field("device_id") deviceId: String,
        @Field("social_id") socialId: String?,
        @Field("device_type") deviceType: String = "android"
    ): BaseResponse<AuthData>

    @FormUrlEncoded
    @POST("sign-up")
    suspend fun register(
        @Field("first_name") firstName: String,
        @Field("last_name") lastName: String,
        @Field("country_code") countryCode: String,
        @Field("phone") phone: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): BaseResponse<AuthData>

    @FormUrlEncoded
    @POST("activate")
    suspend fun activate(
        @Field("code") code: String,
        @Field("email") email: String,
        @Field("device_id") deviceId: String,
        @Field("mac_address") macAddress: String,
        @Field("device_type") deviceType: String = "android",
        @Query("_method") method: String = "patch"
    ): BaseResponse<AuthData>

    @GET("countries")
    suspend fun countries(): BaseResponse<CountriesResponse>

    @FormUrlEncoded
    @POST("forget-password-send-code")
    suspend fun forgetPassword(
        @Field("email") email: String
    ): BaseResponse<AnyResponse>

    @FormUrlEncoded
    @POST("reset-password")
    suspend fun resetPassword(
        @Field("code") code: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): BaseResponse<AnyResponse>

    @GET("resend-code")
    suspend fun resendCode(
        @Query("email") email: String
    ): BaseResponse<AnyResponse>
}
