package com.example.mapsapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mapsapp.ui.components.DrawerMenu
import com.example.mapsapp.ui.screens.Auth.Login.LoginScreen
import com.example.mapsapp.ui.screens.Auth.Login.LoginViewModel
import com.example.mapsapp.ui.screens.Auth.Register.RegisterScreen
import com.example.mapsapp.ui.screens.List.ListScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationWrapper() {
    val backStack = rememberNavBackStack(Route.InitialChecking)
    val currentRoute by remember { derivedStateOf { backStack.last() } }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            DrawerMenu(currentRoute, backStack, scope, drawerState)
        }
    ) {
        Scaffold(
            topBar = {
                if (currentRoute !is Route.Login && currentRoute !is Route.Register && currentRoute !is Route.InitialChecking) {
                    CenterAlignedTopAppBar(
                        title = {
                            val title = drawerMenuItems.find { it.route == currentRoute }?.label ?: "Maps Explorer"
                            Text(
                                title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Default.Menu, 
                                    contentDescription = "Open menu",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            titleContentColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        ) { innerPadding ->
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                modifier = Modifier.padding(innerPadding),
                entryProvider = entryProvider {
                    entry<Route.InitialChecking> {
                        val loginViewModel: LoginViewModel = viewModel()
                        val isLoggedIn by loginViewModel.isLoggedIn.collectAsStateWithLifecycle()
                        val isLoading by loginViewModel.isLoading.collectAsStateWithLifecycle()

                        LaunchedEffect(Unit) {
                            loginViewModel.checkExistingSession()
                        }

                        // Cuando termina de cargar, decidimos a dónde ir
                        LaunchedEffect(isLoading) {
                            if (!isLoading && currentRoute is Route.InitialChecking) {
                                backStack.clear()
                                if (isLoggedIn) {
                                    backStack.add(Route.MapScreen)
                                } else {
                                    backStack.add(Route.Login)
                                }
                            }
                        }

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    entry<Route.Login> {
                        LoginScreen(
                            onLoginSuccess = {
                                backStack.clear()
                                backStack.add(Route.MapScreen)
                            },
                            navigateToRegister = {
                                backStack.add(Route.Register)
                            }
                        )
                    }
                    entry<Route.Register> {
                        RegisterScreen(
                            onRegisterSuccess = {
                                backStack.clear()
                                backStack.add(Route.MapScreen)
                            },
                            onBack = {
                                backStack.removeLastOrNull()
                            }
                        )
                    }

                    entry<Route.MapScreen> {
                        MapScreenNavigation()
                    }

                    entry<Route.LogOut> {
                        val loginViewModel: LoginViewModel = viewModel()
                        LaunchedEffect(Unit) {
                            loginViewModel.logout {
                                backStack.clear()
                                backStack.add(Route.Login)
                            }
                        }
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    entry<Route.ListScreen> { ListScreen() }
                }
            )
        }
    }
}
