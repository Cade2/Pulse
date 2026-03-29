package com.cade2.pulse.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cade2.pulse.ui.auth.LoginScreen
import com.cade2.pulse.ui.auth.RegisterScreen
import com.cade2.pulse.ui.completion.CompletionScreen
import com.cade2.pulse.ui.history.HistoryScreen
import com.cade2.pulse.ui.home.HomeScreen
import com.cade2.pulse.ui.insights.InsightsScreen
import com.cade2.pulse.ui.onboarding.OnboardingScreen
import com.cade2.pulse.ui.profile.ProfileScreen
import com.cade2.pulse.ui.splash.SplashScreen
import com.cade2.pulse.ui.swipe.ContextTagScreen
import com.cade2.pulse.ui.swipe.SwipeScreen

@Composable
fun PulseNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToSwipe = { navController.navigate(Screen.Swipe.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) },
                onNavigateToInsights = { navController.navigate(Screen.Insights.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
            )
        }

        composable(Screen.Swipe.route) {
            SwipeScreen(
                onNavigateToContextTag = { sessionId, acceptedCardIds ->
                    navController.navigate(
                        Screen.ContextTag.createRoute(sessionId, acceptedCardIds)
                    )
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ContextTag.route,
            arguments = listOf(
                navArgument("sessionId") { type = NavType.StringType },
                navArgument("acceptedCardIds") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            ContextTagScreen(
                sessionId = backStackEntry.arguments?.getString("sessionId") ?: "",
                acceptedCardIdsRaw = backStackEntry.arguments?.getString("acceptedCardIds") ?: "",
                onNavigateToCompletion = { streakCount ->
                    navController.navigate(Screen.Completion.createRoute(streakCount)) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onSkip = {
                    navController.navigate(Screen.Completion.createRoute(0)) {
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }

        composable(
            route = Screen.Completion.route,
            arguments = listOf(
                navArgument("streakCount") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            CompletionScreen(
                streakCount = backStackEntry.arguments?.getInt("streakCount") ?: 0,
                onNavigateToInsights = {
                    navController.navigate(Screen.Insights.route) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.Insights.route) {
            InsightsScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
