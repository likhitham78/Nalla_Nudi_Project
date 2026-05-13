package com.example.nalla_nudi.ui.screens

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object Routes {
    const val SPLASH      = "splash"
    const val HOME        = "home"
    const val SEARCH      = "search"
    const val SUBJECT     = "subject/{subject}"
    const val TERM_DETAIL = "term_detail/{termId}"
    const val MY_LIST     = "my_list"
    const val FLASHCARD   = "flashcard"

    fun subjectRoute(subject: String) = "subject/$subject"
    fun termDetailRoute(termId: Int)  = "term_detail/$termId"
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController    = navController,
        startDestination = Routes.SPLASH
    ) {
        // ── Splash ───────────────────────────────────────────
        composable(Routes.SPLASH) {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // ── Home ─────────────────────────────────────────────
        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToMyList    = { navController.navigate(Routes.MY_LIST) },
                onNavigateToFlashcard = { navController.navigate(Routes.FLASHCARD) },
                onNavigateToSubject   = { subject ->
                    navController.navigate(Routes.subjectRoute(subject))
                },
                onNavigateToSearch    = { navController.navigate(Routes.SEARCH) }
            )
        }

        // ── Search ───────────────────────────────────────────
        composable(Routes.SEARCH) {
            SearchScreen(
                onNavigateBack     = { navController.popBackStack() },
                onNavigateToDetail = { termId ->
                    navController.navigate(Routes.termDetailRoute(termId))
                }
            )
        }

        // ── Subject ──────────────────────────────────────────
        composable(Routes.SUBJECT) { backStackEntry ->
            val subject = backStackEntry.arguments?.getString("subject") ?: "All"
            SubjectScreen(
                subject            = subject,
                onNavigateBack     = { navController.popBackStack() },
                onNavigateToDetail = { termId ->
                    navController.navigate(Routes.termDetailRoute(termId))
                }
            )
        }

        // ── Term Detail ──────────────────────────────────────
        composable(Routes.TERM_DETAIL) { backStackEntry ->
            val termId = backStackEntry.arguments?.getString("termId")?.toIntOrNull() ?: 0
            TermDetailScreen(
                termId         = termId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ── My List ──────────────────────────────────────────
        composable(Routes.MY_LIST) {
            MyListScreen(
                onNavigateBack        = { navController.popBackStack() },
                onNavigateToFlashcard = { navController.navigate(Routes.FLASHCARD) }
            )
        }

        // ── Flashcard ────────────────────────────────────────
        composable(Routes.FLASHCARD) {
            FlashcardScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}