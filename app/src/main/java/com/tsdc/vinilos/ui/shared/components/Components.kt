package com.tsdc.vinilos.ui.shared.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.tsdc.vinilos.ui.shared.constants.ColorConstants
import com.tsdc.vinilos.ui.shared.constants.TextSizeConstants

data class NavItem(val label: String, val icon: ImageVector)

val vinilosNavItems = listOf(
    NavItem("Home", Icons.Filled.Home),
    NavItem("Albums", Icons.Filled.Star),
    NavItem("Artists", Icons.Filled.Person),
    NavItem("Collectors", Icons.Filled.Favorite),
)

@Composable
fun VinilosNavBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = Dp(4f)
    ) {
        vinilosNavItems.forEachIndexed { index, item ->
            val isSelected = selectedIndex == index
            val navContentColor = if (isSelected) {
                ColorConstants.primaryBlue
            } else {
                ColorConstants.navUnselected
            }
            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = navContentColor
                    )
                },
                label = {
                    Text(
                        text = item.label.uppercase(),
                        fontSize = TextSizeConstants.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = navContentColor
                    )
                },
                alwaysShowLabel = true,
                colors = vinilosNavigationBarItemColors()
            )
        }
    }
}

@Composable
fun vinilosNavigationBarItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = ColorConstants.primaryBlue,
    selectedTextColor = ColorConstants.primaryBlue,
    indicatorColor = ColorConstants.lightBlue,
    unselectedIconColor = ColorConstants.navUnselected,
    unselectedTextColor = ColorConstants.navUnselected
)
