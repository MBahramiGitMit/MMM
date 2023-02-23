package ir.mehdibahrami.mbmovie.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors()

private val LightColorPalette = lightColors(
    primary = Crayola,
    primaryVariant = Crayola,
    secondary = Teal200,
    background = ChineseBlack,
    onPrimary = ChineseBlack,
    onSecondary = ChineseBlack,
    onSurface = PhilippineSilver
)

@Composable
fun MBMovieTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}