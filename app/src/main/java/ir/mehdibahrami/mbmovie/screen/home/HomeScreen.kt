package ir.mehdibahrami.mbmovie.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ir.mehdibahrami.mbmovie.R
import ir.mehdibahrami.mbmovie.model.net.GenresListItemResponse
import ir.mehdibahrami.mbmovie.model.net.MoviesListResponse
import ir.mehdibahrami.mbmovie.screen.OfflineFooter
import ir.mehdibahrami.mbmovie.screen.OfflineScreen
import ir.mehdibahrami.mbmovie.ui.theme.*

@Composable
fun HomeScreen(
    vm: HomeViewModel = hiltViewModel(),
    onMovieClick: (movieId: Int) -> Unit,
    onGenreClick: (genreId: Int, genreName: String) -> Unit
) {
    val uiController = rememberSystemUiController()

    val topMoviesState = vm.topMoviesState
    val genresListState = vm.genresListState
    val allMoviesState = vm.allMoviesState
    val isOnlineState = vm.isOnline
    val isOnlineForNextState = vm.isOnlineForLoadNext


    SideEffect {
        uiController.setStatusBarColor(color = Crayola)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (isOnlineState) {
            //TopMovies
            if (topMoviesState.isNotEmpty()) {
                item {
                    TopMoviesList(topMoviesState = topMoviesState, onItemClick = onMovieClick)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            //GenreList
            if (genresListState.isNotEmpty()) {
                item {
                    GenresList(genreState = genresListState, onGenreClick = onGenreClick)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            //AllMovies
            items(allMoviesState.items.size) { i ->
                val item = allMoviesState.items[i]
                if (i >= allMoviesState.items.size - 1 && !allMoviesState.endReached && !allMoviesState.isLoading) {
                    vm.loadNextItems()
                }
                MovieItem(item = item, onItemClick = onMovieClick)
            }
            item {
                if (allMoviesState.isLoading) {
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
        } else {
            item {
                OfflineScreen {
                    vm.initLoad()
                }
            }
        }


    }
}

//--------------TopMovie-----------------
@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TopMoviesList(
    topMoviesState: List<MoviesListResponse.Data>,
    onItemClick: (Id: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val pagerState = rememberPagerState()
        HorizontalPager(
            count = topMoviesState.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f, true)
        ) { page ->
            TopMovieItem(item = topMoviesState[page], onItemClick = onItemClick)
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally), activeColor = Color.White
        )
    }
}

@Composable
private fun TopMovieItem(item: MoviesListResponse.Data, onItemClick: (Id: Int) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(item.id!!) },
        contentAlignment = Alignment.BottomCenter
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.poster)
                .crossfade(true)
                .build(),
            contentDescription = "${item.title} poster",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            ChineseBlack
                        )
                    )
                )
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item.title?.let {
                Text(
                    text = it,
                    maxLines = 2,
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                )
            }

            Spacer(modifier = Modifier.requiredHeight(7.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = "Rating",
                    tint = Crayola
                )
                Text(
                    text = "${item.imdbRating ?: "No Rating"} | ${item.country} | ${item.year}",
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        color = PhilippineSilver,
                        fontSize = 16.sp
                    )
                )
            }

            Spacer(modifier = Modifier.requiredHeight(20.dp))

        }
    }
}

//--------------Genre-----------------
@Composable
private fun GenresList(
    genreState: List<GenresListItemResponse>,
    onGenreClick: (Id: Int, name: String) -> Unit
) {
    Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center) {
        Text(
            text = "Genres",
            style = TextStyle(color = Crayola, fontSize = 16.sp),
            modifier = Modifier.padding(start = 13.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        LazyRow(contentPadding = PaddingValues(6.dp)) {
            items(genreState) { genres ->
                GenreItem(item = genres, onGenreClick = onGenreClick)
            }

        }
    }
}

@Composable
private fun GenreItem(
    item: GenresListItemResponse,
    onGenreClick: (Id: Int, name: String) -> Unit
) {
    Text(
        text = item.name,
        modifier = Modifier
            .padding(end = 7.dp)
            .clip(Shapes.large)
            .background(color = RaisinBlack)
            .padding(6.dp)
            .clickable { onGenreClick(item.id, item.name) },
        style = TextStyle(
            color = Color.White,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    )
}

//---------------LastMovie----------------
@Composable
fun MovieItem(item: MoviesListResponse.Data, onItemClick: (Id: Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onItemClick(item.id!!) }
            .border(width = 1.dp, color = MaterialTheme.colors.primary, shape = Shapes.large)
            .padding(6.dp)
    ) {
        AsyncImage(
            model = item.poster,
            contentDescription = "Movie poster",
            modifier = Modifier
                .size(width = 122.dp, height = 183.dp)
                .clip(Shapes.large),
            contentScale = ContentScale.Fit,
            placeholder = painterResource(id = R.drawable.baseline_placeholder_24)
        )

        Column(
            modifier = Modifier.padding(top = 13.dp, start = 8.dp, end = 8.dp),
            verticalArrangement =
            Arrangement.SpaceEvenly
        ) {
            Text(
                modifier = Modifier.padding(bottom = 6.dp),
                text = item.title!!,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp
                ), maxLines = 2, overflow = TextOverflow.Ellipsis
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
                    text = "${item.imdbRating ?: "No Rating"} / 10",
                    style = TextStyle(
                        color = PhilippineSilver,
                        fontSize = 15.sp
                    ), maxLines = 1
                )
            }
            Row(
                modifier = Modifier.padding(bottom = 6.dp),

                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Place,
                    contentDescription = "country",
                    tint = PhilippineSilver
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = item.country ?: "Unknown",
                    style = TextStyle(
                        color = PhilippineSilver,
                        fontSize = 15.sp
                    ), maxLines = 1
                )
            }
            Row(
                modifier = Modifier.padding(bottom = 6.dp),

                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.DateRange,
                    contentDescription = "Year",
                    tint = PhilippineSilver
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = item.year ?: "Unknown",
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        color = PhilippineSilver,
                        fontSize = 15.sp
                    ), maxLines = 1
                )
            }
            Text(
                text = "More Info >",
                style = TextStyle(
                    color = Scarlet,
                    fontSize = 13.sp
                ), maxLines = 1, modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}
