package com.tsdc.vinilos.ui.shared.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val IndigoNeedle = Color(0xFF2B35BD)
val GoldAccent = Color(0xFFF5A623)
val BackgroundGray = Color(0xFFF2F2F6)
val SurfaceWhite = Color(0xFFFFFFFF)
val TextDark = Color(0xFF1A1A2E)
/** Texto secundario; cumple contraste ≥4.5:1 sobre fondos claros. */
val TextMuted = Color(0xFF5C5F66)
val BorderLight = Color(0xFFE5E5EA)

private val LightColors = lightColorScheme(
    primary = IndigoNeedle,
    onPrimary = Color.White,
    secondary = GoldAccent,
    onSecondary = TextDark,
    background = BackgroundGray,
    onBackground = TextDark,
    surface = SurfaceWhite,
    onSurface = TextDark,
    outline = BorderLight
)

private val VinilosTypography = Typography(
    headlineLarge = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.ExtraBold),
    headlineMedium = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.ExtraBold),
    titleLarge = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold),
    titleMedium = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
    bodyLarge = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal),
    bodyMedium = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Normal),
    bodySmall = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal),
    labelSmall = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
)

@Composable
fun VinilosTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = VinilosTypography,
        content = content
    )
}
