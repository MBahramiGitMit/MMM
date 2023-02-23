package ir.mehdibahrami.mbmovie.screen.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import ir.mehdibahrami.mbmovie.model.db.FavoriteMovieEntity
import ir.mehdibahrami.mbmovie.ui.theme.Crayola
import ir.mehdibahrami.mbmovie.ui.theme.Shapes

@Composable
fun FavoriteScreen(vm: FavoriteViewModel = hiltViewModel(), onMovieClick: (movieId: Int) -> Unit) {
    val favoriteMoviesListState = vm.favoriteMoviesListState

    LaunchedEffect(key1 = Unit) {
        vm.loadFavoriteMovies()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(favoriteMoviesListState) { item ->
            FavoriteMovieItem(item = item, onItemClick = onMovieClick)
        }
    }
}

@Composable
private fun FavoriteMovieItem(
    item: FavoriteMovieEntity, onItemClick: (movieId: Int) -> Unit
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .background(MaterialTheme.colors.background)
        .clickable { onItemClick(item.id) }) {
        Card(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = item.poster,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.FillBounds,
            )
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f)))

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            AsyncImage(
                model = item.poster,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .weight(1.5f)
                    .clip(Shapes.medium)
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(2f),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier.padding(), text = item.title, style = TextStyle(
                        fontSize = 25.sp,
                        color = Color.White,
                        shadow = Shadow(color = Color.Black, blurRadius = 10f)
                    ), maxLines = 3, overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier.padding(bottom = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "Rating",
                        tint = Crayola
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${item.imdbRating} / 10", style = TextStyle(
                            fontSize = 20.sp,
                            color = Color.White,
                            shadow = Shadow(color = Color.Black, blurRadius = 10f)
                        ), maxLines = 1
                    )
                }
            }
        }
    }
}