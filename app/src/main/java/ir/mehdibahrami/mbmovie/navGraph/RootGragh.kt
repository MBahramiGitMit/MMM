package ir.mehdibahrami.mbmovie.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ir.mehdibahrami.mbmovie.MainScreen
import ir.mehdibahrami.mbmovie.util.AppScreen
import ir.mehdibahrami.mbmovie.util.Graph
import ir.mehdibahrami.mbmovie.screen.splash.SplashScreen

@Composable
fun RootGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = AppScreen.SplashScreen.route
    ) {
        composable(route = AppScreen.SplashScreen.route) {
            SplashScreen(goToHome = {
                navController.popBackStack()
                navController.navigate(route = Graph.MAIN)
            })
        }
        composable(route = Graph.MAIN) {
            MainScreen()
        }
    }
}