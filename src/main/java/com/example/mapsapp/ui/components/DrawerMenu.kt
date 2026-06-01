package com.example.mapsapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.mapsapp.ui.navigation.DrawerMenuItem
import com.example.mapsapp.ui.navigation.drawerMenuItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerMenu(
    currentRoute: NavKey,
    backStack: NavBackStack<NavKey>,
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        drawerShape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp)
    ) {
        Column(modifier = Modifier.fillMaxHeight().padding(horizontal = 12.dp)) {
            // --- HEADER ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp, horizontal = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Map,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(Modifier.padding(horizontal = 8.dp))
                    Column {
                        Text(
                            text = "Maps Explorer",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Tu guía personal",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
            
            Spacer(Modifier.height(16.dp))

            // --- MENU ITEMS ---
            val mainItems = drawerMenuItems.filter { it !is DrawerMenuItem.Logout }
            mainItems.forEach { item ->
                val isSelected = currentRoute == item.route
                NavigationDrawerItem(
                    icon = { 
                        Icon(
                            item.icon, 
                            contentDescription = item.label,
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        ) 
                    },
                    label = { 
                        Text(
                            item.label, 
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            letterSpacing = 0.5.sp
                        ) 
                    },
                    selected = isSelected,
                    onClick = {
                        backStack.clear()
                        backStack.add(item.route)
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(vertical = 2.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        unselectedContainerColor = Color.Transparent,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- LOGOUT SECTION ---
            val logoutItem = DrawerMenuItem.Logout
            NavigationDrawerItem(
                icon = { 
                    Icon(
                        logoutItem.icon, 
                        contentDescription = logoutItem.label,
                        tint = MaterialTheme.colorScheme.error
                    ) 
                },
                label = { 
                    Text(
                        logoutItem.label, 
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    ) 
                },
                selected = false,
                onClick = {
                    backStack.clear()
                    backStack.add(logoutItem.route)
                    scope.launch { drawerState.close() }
                },
                modifier = Modifier.padding(bottom = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
                )
            )
        }
    }
}
