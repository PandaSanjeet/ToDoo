package com.sanjeetlittle.to_doo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.sanjeetlittle.to_doo.navigation.destinations.listComposable
import com.sanjeetlittle.to_doo.navigation.destinations.taskComposable
import com.sanjeetlittle.to_doo.util.Constants.LIST_SCREEN
import com.sanjeetlittle.to_doo.viewModels.SharedViewModel

@Composable
fun SetupNavigation(navController: NavHostController, sharedViewModel: SharedViewModel){
    val screen = remember(navController) { Screens(navController = navController) }

    NavHost(navController = navController, startDestination = LIST_SCREEN){
        listComposable(navigateToTaskScreen = screen.task, sharedViewModel = sharedViewModel)
        taskComposable(navigateToListScreen = screen.list, sharedViewModel = sharedViewModel)
    }
}