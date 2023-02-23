package ir.mehdibahrami.mbmovie.screen.ganre

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.mehdibahrami.mbmovie.screen.OfflineFooter
import ir.mehdibahrami.mbmovie.screen.OfflineScreen
import ir.mehdibahrami.mbmovie.screen.home.MovieItem

@Composable
fun GenreScreen(
    genreId: Int,
    genreName: String,
    vm: GenreViewModel = hiltViewModel(),
    onMovieClick: (movieId: Int) -> Unit,
    navigateUp: () -> Unit
) {
    val allMoviesByGenreState = vm.allMoviesByGenreState
    val isOnlineState = vm.isOnline
    val isOnlineForNextState = vm.isOnlineForLoadNext

    LaunchedEffect(key1 = Unit) {
        vm.initLoad(genreId = genreId)
    }

    Scaffold(
        topBar = {
            GenreTopBar(title = genreName, navigateUp = navigateUp)
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if(isOnlineState){
                //AllMovies
                items(allMoviesByGenreState.items.size) { i ->
                    val item = allMoviesByGenreState.items[i]
                    if (i >= allMoviesByGenreState.items.size - 1 && !allMoviesByGenreState.endReached && !allMoviesByGenreState.isLoading) {
                        vm.loadNextItems()
                    }
                    MovieItem(item = item, onItemClick = onMovieClick)
                }
                item {
                    if (allMoviesByGenreState.isLoading) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                item {
                    if (!isOnlineForNextState) {
                        OfflineFooter {
                            vm.loadNextItems()
                        }
                    }
                }
            }else{
                item {
                    OfflineScreen {
                        vm.loadFirstItems()
                    }
                }
            }
        }
    }
}

@Composable
private fun GenreTopBar(title: String, navigateUp: () -> Unit) {
    TopAppBar(
        title = ({ Text(text = title) }),
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
            }
        })
}