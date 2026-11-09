package com.example.progr3sshabitplannertracker

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object ResetPassword : Screen("reset_password")
    object Home : Screen("home")
    object CreateSchedule : Screen("create_schedule")
}
