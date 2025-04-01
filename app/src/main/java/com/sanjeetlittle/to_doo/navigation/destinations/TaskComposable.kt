package com.sanjeetlittle.to_doo.navigation.destinations

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sanjeetlittle.to_doo.ui.screens.task.TaskScreen
import com.sanjeetlittle.to_doo.util.Action
import com.sanjeetlittle.to_doo.util.Constants
import com.sanjeetlittle.to_doo.util.Constants.TASK_ARGUMENT_KEY
import com.sanjeetlittle.to_doo.viewModels.SharedViewModel

fun NavGraphBuilder.taskComposable(navigateToListScreen: (Action) -> Unit, sharedViewModel: SharedViewModel) {
    composable(
        route = Constants.TASK_SCREEN,
        arguments = listOf(navArgument(TASK_ARGUMENT_KEY) {
            type = NavType.IntType
        })
    ) {
        val taskId = it.arguments!!.getInt(TASK_ARGUMENT_KEY)
        LaunchedEffect(key1 = taskId){
            sharedViewModel.getSelectedTask(taskId)
        }
        val selectedTask by sharedViewModel.selectedTask.collectAsState()

        LaunchedEffect(key1 = selectedTask){
            if (selectedTask != null || taskId == -1){
                sharedViewModel.updateTaskFields(selectedTask = selectedTask)
            }
        }

        TaskScreen(selectedTask, sharedViewModel, navigateToListScreen = navigateToListScreen)
    }
}