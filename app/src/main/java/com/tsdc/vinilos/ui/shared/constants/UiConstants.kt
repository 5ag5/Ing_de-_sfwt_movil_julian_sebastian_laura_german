package com.tsdc.vinilos.ui.shared.constants

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Constantes de dimensiones para el proyecto
 * Evita magic numbers en el código
 */
object DimensionConstants {
    // Padding y spacing
    val paddingXSmall = 4.dp
    val paddingSmall = 8.dp
    val paddingMedium = 12.dp
    val paddingDefault = 16.dp
    val paddingLarge = 20.dp
    val paddingXLarge = 24.dp

    // Altura de elementos
    val buttonHeight = 48.dp
    val fabSize = 52.dp
    val iconSize = 24.dp
    val searchFieldHeight = 50.dp

    // Rounded corners
    val cornerSmall = 8.dp
    val cornerMedium = 12.dp
    val cornerLarge = 14.dp
    val cornerXLarge = 16.dp

    // Tamaño de imagen
    val albumCoverAspectRatio = 1.4f

    // Elevation
    val elevationSmall = 2.dp
    val elevationMedium = 4.dp
    val elevationLarge = 8.dp
}

/**
 * Constantes de tamaño de texto (Font Sizes)
 */
object TextSizeConstants {
    val titleLarge = 26.sp
    val titleMedium = 24.sp
    val titleSmall = 22.sp
    val headlineSmall = 18.sp
    val headlineXSmall = 16.sp
    val bodyLarge = 15.sp
    val bodyMedium = 14.sp
    val bodySmall = 12.sp
    val labelSmall = 10.sp
}

/**
 * Constantes de colores (Centralización)
 * Nota: Estos también están en colors.xml para Android Resources
 */
object ColorConstants {
    // Primary
    val primaryBlue = Color(0xFF2B35BD)
    val lightBlue = Color(0xFFE8EAFF)

    // Grayscale
    val darkGray = Color(0xFF1A1A2E)
    val mediumGray = Color(0xFF191B24)
    val lightGray = Color(0xFFA0A7B5)
    val borderGray = Color(0xFFE1E4F2)
    val textLight = Color(0xFF4D4D4D)

    val navUnselected = Color(0xFF5C5F66)

    val accentGoldAccessible = Color(0xFF7A5200)

    // Semantic
    val errorRed = Color(0xFFB00020)
    val lightBackground = Color(0xFFF2F2F6)
    val white = Color.White
    val black = Color.Black
}