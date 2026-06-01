package com.example.mapsapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*


import androidx.compose.ui.graphics.vector.ImageVector

sealed class DrawerMenuItem(val route: Route, val label: String, val icon: ImageVector) {
    data object Item1 : DrawerMenuItem(Route.MapScreen, "Mapa", Icons.Default.Map)
    data object Item2 : DrawerMenuItem(Route.ListScreen, "Lista de Localizaciones", Icons.Default.List)
    data object Logout : DrawerMenuItem(Route.LogOut, "Cerrar Sesión",Icons.AutoMirrored.Filled.Logout)
}
val drawerMenuItems = listOf(DrawerMenuItem.Item1, DrawerMenuItem.Item2, DrawerMenuItem.Logout)
