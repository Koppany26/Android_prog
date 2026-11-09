package com.example.progr3sshabitplannertracker.data.network

import com.example.progr3sshabitplannertracker.data.model.AuthResponseDto
import com.example.progr3sshabitplannertracker.data.model.CreateHabitDto
import com.example.progr3sshabitplannertracker.data.model.CreateProgressDto
import com.example.progr3sshabitplannertracker.data.model.HabitCategory
import com.example.progr3sshabitplannertracker.data.model.HabitResponse
import com.example.progr3sshabitplannertracker.data.model.ProfileResponseDto
import com.example.progr3sshabitplannertracker.data.model.ProgressResponseDto
import com.example.progr3sshabitplannertracker.data.model.ResetPasswordDto
import com.example.progr3sshabitplannertracker.data.model.ScheduleCreateRequest
import com.example.progr3sshabitplannertracker.data.model.ScheduleResponse
import com.example.progr3sshabitplannertracker.data.model.SignInDto
import com.example.progr3sshabitplannertracker.data.model.SignUpDto
import com.example.progr3sshabitplannertracker.data.model.UpdateProfileDto
import com.example.progr3sshabitplannertracker.data.model.UpdateScheduleDto
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("auth/local/signin")
    fun signIn(@Body request: SignInDto): Call<AuthResponseDto>

    @POST("auth/local/signin")
    suspend fun login(@Body request: SignInDto): Response<AuthResponseDto>

    @POST("auth/local/signup")
    fun signUp(@Body request: SignUpDto): Call<AuthResponseDto>

    @POST("auth/local/signup")
    suspend fun signup(@Body request: SignUpDto): Response<AuthResponseDto>

    @POST("auth/reset-password-via-email")
    fun resetPassword(@Body request: ResetPasswordDto): Call<Void>

    @POST("auth/reset-password-via-email")
    suspend fun resetPasswordViaEmail(@Body request: ResetPasswordDto): Response<Void>

    @POST("auth/local/logout")
    suspend fun logout(): Response<Void>

    // Profile
    @GET("profile")
    suspend fun getProfile(): Response<ProfileResponseDto>

    @PATCH("profile")
    suspend fun updateProfile(@Body body: UpdateProfileDto): Response<ProfileResponseDto>

    @Multipart
    @POST("profile/upload-profile-image")
    suspend fun uploadProfileImage(
        @Part profileImage: MultipartBody.Part
    ): Response<ProfileResponseDto>
    
    // Habits
    @GET("habit/categories")
    suspend fun getHabitCategories(): Response<List<HabitCategory>>

    @GET("habit")
    suspend fun getHabits(): Response<List<HabitResponse>>

    @POST("habit")
    suspend fun createHabit(@Body body: CreateHabitDto): Response<HabitResponse>

    // Schedules
    @GET("schedule/day")
    suspend fun getScheduleByDay(@Query("date") date: String): Response<List<ScheduleResponse>>

    @GET("schedule/{id}")
    suspend fun getScheduleById(@Path("id") id: Long): Response<ScheduleResponse>

    @POST("schedule/recurring")
    suspend fun createRecurringSchedule(@Body body: ScheduleCreateRequest): Response<List<ScheduleResponse>>

    @POST("schedule/custom")
    suspend fun createCustomSchedule(@Body body: ScheduleCreateRequest): Response<ScheduleResponse>

    @PATCH("schedule/{id}")
    suspend fun updateSchedule(@Path("id") id: Long, @Body body: UpdateScheduleDto): Response<ScheduleResponse>

    @DELETE("schedule/{id}")
    suspend fun deleteSchedule(@Path("id") id: Long): Response<Void>

    // Progress
    @POST("progress")
    suspend fun createProgress(@Body body: CreateProgressDto): Response<ProgressResponseDto>
}
