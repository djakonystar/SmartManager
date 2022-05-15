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
    @POST("/auth/login")
    fun signIn(
        @Body signIn: SignInPost
    ): Observable<GenericResponse<SignIn>>

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

    @GET("/bot/byDate")
    fun getReportsByDate(
        @Header("Authorization") token: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): Observable<GenericResponse<List<Report>>>

    @GET("/bot/byDateSupervisor/{id}")
    fun getSupervisorsReports(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("start") start: String,
        @Query("end") end: String
    ): Observable<GenericResponse<List<Report>>>

    @GET("/user")
    fun getAdmins(
        @Header("Authorization") token: String,
    ): Observable<GenericResponse<List<Admin>>>

    @POST("/user/addUser")
    fun addAdmin(
        @Header("Authorization") token: String,
        @Body admin: AdminPost
    ): Observable<GenericResponse<Any?>>

    @PUT("/user/editUser/{id}")
    fun editAdmin(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body admin: AdminPost
    ): Observable<GenericResponse<Any?>>

    @DELETE("/user/deleteUser/{id}")
    fun deleteAdmin(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Observable<GenericResponse<Any?>>
}
