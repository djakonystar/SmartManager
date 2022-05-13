package uz.texnopos.smartmanager.data.remote

import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.*
import uz.texnopos.smartmanager.data.models.GenericResponse
import uz.texnopos.smartmanager.data.models.bot.EditChatId
import uz.texnopos.smartmanager.data.models.bot.EditTime
import uz.texnopos.smartmanager.data.models.bot.Rule
import uz.texnopos.smartmanager.data.models.signin.SignIn
import uz.texnopos.smartmanager.data.models.user.Supervisor

interface ApiInterface {
    @POST("/auth/login")
    fun signIn(
        @Body signIn: SignIn
    ): Response<Observable<GenericResponse<String?>>>

    @PUT("/bot/editTime")
    fun editTime(
        @Header("Authorization") token: String,
        @Body editTime: EditTime
    ): Observable<GenericResponse<Any?>>

    @PUT("/bot/editChatId")
    fun editChatId(
        @Header("Authorization") token: String,
        @Body editChatId: EditChatId
    ): Observable<GenericResponse<Any?>>

    @GET("/bot/getRule")
    fun getRule(
        @Header("Authorization") token: String,
    ): Observable<GenericResponse<Rule>>

    @GET("/bot/getAllSupervisor")
    fun getSupervisors(
        @Header("Authorization") token: String,
    ): Observable<GenericResponse<List<Supervisor>>>
}