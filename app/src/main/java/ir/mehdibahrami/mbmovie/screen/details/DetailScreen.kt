package ir.mehdibahrami.mbmovie.screen.details

import android.content.Intent
import android.net.Uri
import android.widget.FrameLayout
import android.widget.ListPopupWindow.MATCH_PARENT
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.google.accompanist.pager.*
import ir.mehdibahrami.mbmovie.R
import ir.mehdibahrami.mbmovie.model.net.MovieDetailsResponse
import ir.mehdibahrami.mbmovie.screen.OfflineScreen
import ir.mehdibahrami.mbmovie.ui.theme.*
import ir.mehdibahrami.mbmovie.util.Constants
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.math.absoluteValue

@Composable
fun DetailsScreen(movieId: Int, vm: DetailViewModel = hiltViewModel(), navigateUp: () -> Unit) {

    val context = LocalContext.current

    val isLoadingState = vm.isLoadingState
    val movieDetailState = vm.movieDetailState
    val isFavoriteMovieState = vm.isFavoriteMovieState
    val isOnlineState = vm.isOnline
    val onFavoriteIconClicked: () -> Unit = { vm.onFavoriteIconClicked() }
    val scope = rememberCoroutineScope()
    val downloadIntent = remember { Intent(Intent.ACTION_VIEW, Uri.parse(Constants.VIDEO_URL)) }

    LaunchedEffect(key1 = Unit) {
        vm.initLoad(movieId)
    }

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (isOnlineState) {
            if (isLoadingState) {
                CircularProgressIndicator(Modifier.padding(15.dp))
            } else {
                DetailsTopBar(
                    title = movieDetailState.title!!,
                    isFavoriteMovie = isFavoriteMovieState,
                    onFavoriteIconClicked = onFavoriteIconClicked,
                    navigateUp = navigateUp
                )
                DetailsHeader(movieDetails = movieDetailState,
                    onDownloadClicked = { context.startActivity(downloadIntent) },
                    onViewClicked = { scope.launch { scrollState.animateScrollTo(value = scrollState.maxValue) } })
                Spacer(modifier = Modifier.height(8.dp))
                DetailsPager(movieDetails = movieDetailState)
                movieDetailState.images?.let { ImagesSlider(images = it) }
                PlayerItem()
            }
        } else {
            OfflineScreen {
                vm.initLoad(movieId)
            }
        }
    }
}

@Composable
private fun DetailsTopBar(
    title: String,
    isFavoriteMovie: Boolean,
    onFavoriteIconClicked: () -> Unit,
    navigateUp: () -> Unit
) {
    TopAppBar(title = ({ Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) }),
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = onFavoriteIconClicked) {
                if (isFavoriteMovie) {
                    Icon(
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = "favoriteIcon",
                        tint = Scarlet
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = "favoriteIcon",
                        tint = Color.Black
                    )
                }


            }
        })
}

@Composable
private fun DetailsHeader(
    movieDetails: MovieDetailsResponse, onDownloadClicked: () -> Unit, onViewClicked: () -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(260.dp)
    ) {
        AsyncImage(
            model = movieDetails.poster,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ChineseBlack.copy(alpha = 0.90f))
        )

        Row(
            Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = movieDetails.poster,
                contentDescription = "MoviePoster",
                modifier = Modifier
                    .fillMaxHeight()
                    .clip(
                        Shapes.medium.copy(
                            topStart = CornerSize(0.dp)
                        )
                    )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column(
                Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly
            ) {
                //Director
                Row(
                    modifier = Modifier.padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Campaign,
                        contentDescription = "director",
                        tint = Crayola
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = movieDetails.director ?: "Unknown",
                        color = PhilippineSilver,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
                //Imdb
                Row(
                    modifier = Modifier.padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = R.drawable.imdb_icon,
                        contentDescription = "imdb",
                        modifier = Modifier
                            .width(28.dp)
                            .clip(Shapes.small),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${movieDetails.imdbRating ?: "Unknown"} (${movieDetails.imdbVotes})",
                        color = PhilippineSilver,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }

                //tomato
                Row(
                    modifier = Modifier.padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    AsyncImage(
                        model = R.drawable.meta_icon,
                        contentDescription = "tomato",
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    val tomatoRating: String = remember {
                        val df = DecimalFormat("###,###")
                        "${(15..100).random()} (${df.format((1000..1000000).random())})"
                    }
                    Text(
                        text = tomatoRating,
                        color = PhilippineSilver,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }

                //runtime
                Row(
                    modifier = Modifier.padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Icon(
                        imageVector = Icons.Outlined.Timer,
                        contentDescription = "runtime",
                        tint = Crayola,
                        modifier = Modifier.size(28.dp),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = movieDetails.runtime ?: "Unknown",
                        color = PhilippineSilver,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }

                //runtime
                Row(
                    modifier = Modifier.padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Icon(
                        imageVector = Icons.Outlined.Language,
                        contentDescription = "runtime",
                        tint = Crayola,
                        modifier = Modifier.size(28.dp),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = movieDetails.country ?: "Unknown",
                        color = PhilippineSilver,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { onDownloadClicked() }) {
                        Text(text = "Download", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { onViewClicked() },
                        shape = CircleShape,
                        border = BorderStroke(2.dp, color = Color.Black),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Preview,
                            contentDescription = "preview",
                            tint = Color.Black
                        )
                    }
                }


            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun DetailsPager(movieDetails: MovieDetailsResponse) {
    val pages = remember {
        listOf("Info", "Awards", "More")
    }
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    TabRow(
        // Our selected tab is our current page
        selectedTabIndex = pagerState.currentPage,
        // Override the indicator, using the provided pagerTabIndicatorOffset modifier
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }, backgroundColor = MaterialTheme.colors.background, contentColor = Crayola
    ) {
        // Add tabs for all of our pages
        pages.forEachIndexed { index, title ->
            Tab(text = { Text(title) }, selected = pagerState.currentPage == index, onClick = {
                scope.launch { pagerState.animateScrollToPage(index) }
            }, selectedContentColor = Color.White
            )
        }
    }

    HorizontalPager(
        count = pages.size, state = pagerState, verticalAlignment = Alignment.Top
    ) { page ->
        when (page) {
            0 -> StoryLinePage(storyline = movieDetails.plot ?: "Unknown")
            1 -> AwardsPage(awards = movieDetails.awards ?: "Unknown")
            2 -> MorePage(actors = movieDetails.actors ?: "Unknown")
        }
    }
}

@Composable
private fun StoryLinePage(storyline: String) {
    Column(
        modifier = Modifier
            .padding(6.dp)
            .height(94.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Storyline", style = Typography.h6.copy(color = Color.White))
        Text(
            text = storyline,
            style = Typography.body1.copy(color = PhilippineSilver),
            textAlign = TextAlign.Justify
        )
    }
}

@Composable
private fun AwardsPage(awards: String) {
    Column(
        modifier = Modifier
            .padding(6.dp)
            .height(94.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Awards", style = Typography.h6.copy(color = Color.White))
        Text(
            text = awards,
            style = Typography.body1.copy(color = PhilippineSilver),
            textAlign = TextAlign.Justify
        )
    }
}

@Composable
private fun MorePage(actors: String) {
    Column(
        modifier = Modifier
            .padding(6.dp)
            .height(94.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Actors", style = Typography.h6.copy(color = Color.White))
        Text(
            text = actors,
            style = Typography.body1.copy(color = PhilippineSilver),
            textAlign = TextAlign.Justify
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ImagesSlider(images: List<String?>) {

    Column {
        Text(
            text = "Actors",
            style = Typography.h6.copy(color = Color.White),
            modifier = Modifier.padding(6.dp)
        )


        val pagerState = rememberPagerState()
        HorizontalPager(
            count = images.size,
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 48.dp)
        ) { page ->
            Card(
                Modifier
                    .graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                        lerp(
                            start = ScaleFactor(
                                scaleX = 0.85f, scaleY = 0.85f
                            ), stop = ScaleFactor(
                                scaleX = 1f, scaleY = 1f
                            ), fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale.scaleX
                            scaleY = scale.scaleY
                        }
                    }
                    .fillMaxWidth()
                    .aspectRatio(1.78f)
                    .background(Gunmetal),
                border = BorderStroke(width = 1.dp, color = Crayola)) {
                AsyncImage(
                    model = images[page],
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Gunmetal)
                )
            }
        }
    }
}

@Composable
private fun PlayerItem(videoURL: String = Constants.VIDEO_URL) {

    Column {
        Text(
            text = "Video",
            style = Typography.h6.copy(color = Color.White),
            modifier = Modifier.padding(6.dp)
        )

        VideoPlayer(videoURL = videoURL)
    }
}

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
private fun VideoPlayer(videoURL: String) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val defaultDataSourceFactory = DefaultDataSource.Factory(context)
            val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(
                context, defaultDataSourceFactory
            )
            val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(videoURL))

            setMediaSource(source)
            prepare()
        }

    }
    exoPlayer.playWhenReady = false
    exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

    Card(
        shape = Shapes.medium,
        modifier = Modifier
            .padding(start = 6.dp, end = 6.dp, bottom = 6.dp)
            .aspectRatio(1.78f),
        border = BorderStroke(width = 1.dp, color = Crayola)
    ) {
        DisposableEffect(
            AndroidView(factory = {
                PlayerView(context).apply {
                    useController = true
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

                    player = exoPlayer
                    layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                }
            })
        ) {
            onDispose { exoPlayer.release() }
        }
    }
}
