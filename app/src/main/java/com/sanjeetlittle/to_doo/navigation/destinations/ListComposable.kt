package com.sanjeetlittle.to_doo.navigation.destinations

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sanjeetlittle.to_doo.ui.screens.list.ListScreen
import com.sanjeetlittle.to_doo.util.Action
import com.sanjeetlittle.to_doo.util.Constants.LIST_ARGUMENT_KEY
import com.sanjeetlittle.to_doo.util.Constants.LIST_SCREEN
import com.sanjeetlittle.to_doo.util.toAction
import com.sanjeetlittle.to_doo.viewModels.SharedViewModel


fun NavGraphBuilder.listComposable( navigateToTaskScreen: (taskId: Int) -> Unit, sharedViewModel: SharedViewModel){
    composable(
        route = LIST_SCREEN,
        arguments = listOf(navArgument(LIST_ARGUMENT_KEY){
            type = NavType.StringType
            //defaultValue = "A"
            nullable = true
        })
    ){
        val action = it.arguments?.getString(LIST_ARGUMENT_KEY).toAction()

        var myAction by rememberSaveable {
            mutableStateOf(Action.NO_ACTION)
        }

        LaunchedEffect(key1 = myAction){
            if (action != myAction){
                myAction = action
                sharedViewModel.updateAction(action)
            }
        }

        val databaseAction = sharedViewModel.action

        ListScreen(action = databaseAction, navigateToTaskScreen=navigateToTaskScreen, sharedViewModel = sharedViewModel)
    }
}
