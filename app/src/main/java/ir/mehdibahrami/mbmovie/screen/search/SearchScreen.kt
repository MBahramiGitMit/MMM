package ir.mehdibahrami.mbmovie.screen.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.mehdibahrami.mbmovie.R
import ir.mehdibahrami.mbmovie.screen.OfflineFooter
import ir.mehdibahrami.mbmovie.screen.OfflineScreen
import ir.mehdibahrami.mbmovie.screen.home.MovieItem
import ir.mehdibahrami.mbmovie.ui.theme.Crayola
import ir.mehdibahrami.mbmovie.ui.theme.Gunmetal
import ir.mehdibahrami.mbmovie.ui.theme.PhilippineSilver

@Composable
fun SearchScreen(vm: SearchViewModel = hiltViewModel(), onMovieClick: (movieId: Int) -> Unit) {
    val searchFieldValueState = vm.searchFieldValueState
    val searchedMoviesScreenState = vm.searchedMoviesScreenState
    val isOnlineState = vm.isOnline
    val isOnlineForNextState = vm.isOnlineForLoadNext
    LazyColumn(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            SearchField(value = searchFieldValueState, onValueChange = { newValue ->
                vm.onSearchFieldValueChange(newValue)
            })
        }
        if (isOnlineState) {
            item {
                if (searchedMoviesScreenState.items.isEmpty() && !searchedMoviesScreenState.isLoading) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.empty),
                            contentDescription = null
                        )
                        Text(text = "empty")
                    }
                }
            }
            items(searchedMoviesScreenState.items.size) { i ->
                val item = searchedMoviesScreenState.items[i]
                if (i >= searchedMoviesScreenState.items.size - 1 && !searchedMoviesScreenState.endReached && !searchedMoviesScreenState.isLoading) {
                    vm.loadNextItems()
                }
                MovieItem(item = item, onItemClick = onMovieClick)
            }
            item {
                if (searchedMoviesScreenState.isLoading) {
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
                    vm.loadFirstItems()
                }
            }
        }

    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchField(value: String, onValueChange: (newValue: String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = stringResource(id = R.string.search_field_placeholder),
                fontSize = 18.sp,
                modifier = Modifier.alpha(ContentAlpha.medium),
            )
        },
        leadingIcon = {
            Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
        colors = TextFieldDefaults.textFieldColors(
            textColor = PhilippineSilver,
            backgroundColor = Gunmetal,
            cursorColor = Crayola,
            leadingIconColor = PhilippineSilver.copy(alpha = 0.8f),
            placeholderColor = PhilippineSilver.copy(alpha = 0.8f),
            focusedIndicatorColor = Crayola,
            unfocusedIndicatorColor = Crayola
        ),
        textStyle = TextStyle(fontSize = 19.sp, fontWeight = FontWeight.Bold)
    )
}