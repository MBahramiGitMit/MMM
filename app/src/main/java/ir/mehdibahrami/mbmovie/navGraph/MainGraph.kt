package ir.mehdibahrami.mbmovie.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ir.mehdibahrami.mbmovie.util.AppScreen
import ir.mehdibahrami.mbmovie.util.Graph
import ir.mehdibahrami.mbmovie.screen.details.DetailsScreen
import ir.mehdibahrami.mbmovie.screen.favorite.FavoriteScreen
import ir.mehdibahrami.mbmovie.screen.ganre.GenreScreen
import ir.mehdibahrami.mbmovie.screen.home.HomeScreen
import ir.mehdibahrami.mbmovie.screen.search.SearchScreen

@Composable
fun MainNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = AppScreen.HomeScreen.route
    ) {
        composable(route = AppScreen.HomeScreen.route) {
            HomeScreen(onMovieClick = { movieId ->
                navController.navigate(AppScreen.DetailsScreen.route + "/$movieId")
            }, onGenreClick = { genreId, genreName ->
                navController.navigate(AppScreen.GenreScreen.route + "/$genreId" + "/$genreName")
            })
        }
        composable(route = AppScreen.SearchScreen.route) {
            SearchScreen(onMovieClick = { movieId ->
                navController.navigate(AppScreen.DetailsScreen.route + "/$movieId")
            })
        }
        composable(route = AppScreen.FavoriteScreen.route) {
            FavoriteScreen(onMovieClick = { movieId ->
                navController.navigate(AppScreen.DetailsScreen.route + "/$movieId")
            })
        }
        composable(
            route = AppScreen.DetailsScreen.route + "/{movie_id}",
            arguments = listOf(navArgument("movie_id") {
                type = NavType.IntType
            })
        ) {
            DetailsScreen(movieId = it.arguments!!.getInt("movie_id"),
                navigateUp = { navController.navigateUp() })
        }
        composable(
            route = AppScreen.GenreScreen.route + "/{genre_id}/{genre_name}",
            arguments = listOf(navArgument("genre_id") {
                type = NavType.IntType
            }, navArgument("genre_name") {
                type = NavType.StringType
            })
        ) {
            GenreScreen(genreId = it.arguments!!.getInt("genre_id"),
                genreName = it.arguments!!.getString("genre_name").toString(),
                onMovieClick = { movieId ->
                    navController.navigate(AppScreen.DetailsScreen.route + "/$movieId")
                },
                navigateUp = { navController.navigateUp() })
        }


    }
}





