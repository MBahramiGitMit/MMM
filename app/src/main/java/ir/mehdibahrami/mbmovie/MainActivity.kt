package ir.mehdibahrami.mbmovie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.mehdibahrami.mbmovie.navGraph.MainNavGraph
import ir.mehdibahrami.mbmovie.navGraph.RootGraph
import ir.mehdibahrami.mbmovie.ui.theme.ChineseBlack
import ir.mehdibahrami.mbmovie.ui.theme.Crayola
import ir.mehdibahrami.mbmovie.ui.theme.MBMovieTheme
import ir.mehdibahrami.mbmovie.ui.theme.PhilippineSilver
import ir.mehdibahrami.mbmovie.util.AppScreen
import ir.mehdibahrami.mbmovie.util.BottomNavItem

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MBMovieTheme(darkTheme = false) {
                RootGraph(navController = rememberNavController())
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) {
        Surface(modifier = Modifier.padding(bottom = it.calculateBottomPadding()), color = MaterialTheme.colors.background) {
            MainNavGraph(navController = navController)
        }

    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val screens = listOf(
        BottomNavItem(
            route = AppScreen.HomeScreen.route,
            label = stringResource(R.string.home),
            icon = Icons.Default.Home
        ),
        BottomNavItem(
            route = AppScreen.SearchScreen.route,
            label = stringResource(R.string.search),
            icon = Icons.Default.Search
        ),
        BottomNavItem(
            route = AppScreen.FavoriteScreen.route,
            label = stringResource(R.string.favorite),
            icon = Icons.Default.Favorite
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomNavBarDestination) {
        BottomNavigation(
            backgroundColor = ChineseBlack
        ) {
            screens.forEach {
                AddItem(
                    screen = it,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomNavItem,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        label = {
            Text(text = screen.label)
        },
        alwaysShowLabel = false,
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        unselectedContentColor = PhilippineSilver,
        selectedContentColor = Crayola,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}