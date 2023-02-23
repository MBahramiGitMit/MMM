package ir.mehdibahrami.mbmovie.util

import androidx.compose.ui.graphics.vector.ImageVector

sealed class AppScreen(val route: String) {
    object SplashScreen : AppScreen(route = "Splash")
    object HomeScreen : AppScreen(route = "Home")
    object SearchScreen : AppScreen(route = "Search")
    object DetailsScreen : AppScreen(route = "Detail")
    object FavoriteScreen : AppScreen(route = "Favorite")
    object GenreScreen : AppScreen(route = "Genre")
}

object Graph {
    const val ROOT = "root_graph"
    const val MAIN = "home_graph"
//    const val AUTHENTICATION = "auth_graph"
}

data class BottomNavItem(val route: String, val label: String, val icon: ImageVector)