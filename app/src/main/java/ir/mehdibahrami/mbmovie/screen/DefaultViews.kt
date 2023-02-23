package ir.mehdibahrami.mbmovie.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.mehdibahrami.mbmovie.ui.theme.PhilippineSilver


@Composable
fun OfflineScreen(onButtonClicked: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(120.dp))
        Icon(
            modifier = Modifier.size(100.dp),
            imageVector = Icons.Rounded.CloudOff,
            contentDescription = "offline",
            tint = PhilippineSilver
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(text = "No InternetConnection", color = PhilippineSilver, fontSize = 21.sp)
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = onButtonClicked) {
            Text(text = "Try Again")
        }
    }
}

@Composable
fun OfflineFooter(onButtonClicked: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "No InternetConnection", color = PhilippineSilver, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Button(onClick = onButtonClicked) {
            Text(text = "Try Again")
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}