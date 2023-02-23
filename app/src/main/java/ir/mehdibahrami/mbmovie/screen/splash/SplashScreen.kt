package ir.mehdibahrami.mbmovie.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.mehdibahrami.mbmovie.R
import ir.mehdibahrami.mbmovie.util.Constants
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(goToHome: () -> Unit) {
    LaunchedEffect(key1 = Unit) {
        delay(1000)
        goToHome()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "app logo",
            modifier = Modifier
                .size(200.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = Constants.APP_NAME,
            style = TextStyle(
                fontSize = 17.sp,
                color = Color.White,
                fontFamily = FontFamily.SansSerif
            )
        )
    }
}