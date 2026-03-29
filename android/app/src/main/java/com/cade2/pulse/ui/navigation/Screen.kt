package com.cade2.pulse.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Onboarding : Screen("onboarding")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object Swipe : Screen("swipe")

    data object ContextTag : Screen("context_tag/{sessionId}/{acceptedCardIds}") {
        fun createRoute(sessionId: String, acceptedCardIds: String) =
            "context_tag/$sessionId/$acceptedCardIds"
    }

    data object Completion : Screen("completion/{streakCount}") {
        fun createRoute(streakCount: Int) = "completion/$streakCount"
    }

    data object History : Screen("history")
    data object Insights : Screen("insights")
    data object Profile : Screen("profile")
}
