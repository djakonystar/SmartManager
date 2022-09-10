package uz.texnopos.smartmanager.data.remote

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*
import uz.texnopos.smartmanager.data.models.GenericResponse
import uz.texnopos.smartmanager.data.models.bot.EditChatId
import uz.texnopos.smartmanager.data.models.bot.EditTime
import uz.texnopos.smartmanager.data.models.bot.Rule
import uz.texnopos.smartmanager.data.models.report.Report
import uz.texnopos.smartmanager.data.models.signin.SignIn
import uz.texnopos.smartmanager.data.models.signin.SignInPost
import uz.texnopos.smartmanager.data.models.user.Admin
import uz.texnopos.smartmanager.data.models.user.AdminPost
import uz.texnopos.smartmanager.data.models.user.Supervisor

interface ApiInterface {
    @POST("api/v1/login")
    fun signIn(
        @Body signIn: SignInPost
    ): Observable<GenericResponse<SignIn>>

    @PATCH("api/v1/bot/setting")
    fun editTime(
        @Header("Authorization") token: String,
        @Body editTime: EditTime
    ): Observable<GenericResponse<Any?>>

    @PUT("api/v1/bot/chatId")
    fun editChatId(
        @Header("Authorization") token: String,
        @Body editChatId: EditChatId
    ): Observable<GenericResponse<Any?>>

    @GET("api/v1/bot/setting")
    fun getRule(
        @Header("Authorization") token: String,
    ): Observable<GenericResponse<List<Rule>>>

    @GET("api/v1/getAllSupervisor")
    fun getSupervisors(
        @Header("Authorization") token: String,
    ): Observable<GenericResponse<List<Supervisor>>>

    @GET("api/v1/byDate")
    fun getReportsByDate(
        @Header("Authorization") token: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): Observable<GenericResponse<List<Report>>>

    @GET("api/v1/byDateSupervisor/{id}")
    fun getSupervisorsReports(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("start") start: String,
        @Query("end") end: String
    ): Observable<GenericResponse<List<Report>>>

    @GET("api/v1/user")
    fun getAdmins(
        @Header("Authorization") token: String,
    ): Observable<GenericResponse<List<Admin>>>

    @POST("api/v1/user")
    fun addAdmin(
        @Header("Authorization") token: String,
        @Body admin: AdminPost
    ): Observable<GenericResponse<Any?>>

    @PUT("api/v1/editUser/{id}")
    fun editAdmin(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body admin: AdminPost
    ): Observable<GenericResponse<Any?>>

    @DELETE("api/v1/user/deleteUser/{id}")
    fun deleteAdmin(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Observable<GenericResponse<Any?>>
}
