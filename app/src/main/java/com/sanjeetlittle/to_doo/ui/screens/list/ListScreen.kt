package com.sanjeetlittle.to_doo.ui.screens.list

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.sanjeetlittle.to_doo.R
import com.sanjeetlittle.to_doo.util.Action
import com.sanjeetlittle.to_doo.util.SearchAppBarState
import com.sanjeetlittle.to_doo.viewModels.SharedViewModel
import kotlinx.coroutines.launch

@Composable
fun ListScreen(
    action: Action,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {

    LaunchedEffect(key1 = action){
        sharedViewModel.handleDatabaseActions(action = action)
    }

    val allTasks by sharedViewModel.allTasks.collectAsState()

    val searchedTasks by sharedViewModel.searchedTasks.collectAsState()

    val sortState by sharedViewModel.sortState.collectAsState()
    val lowPriorityTasks by sharedViewModel.lowPriorityTasks.collectAsState()
    val highPriorityTasks by sharedViewModel.highPriorityTasks.collectAsState()

    val searchAppBarState: SearchAppBarState = sharedViewModel.searchAppBarState
    val searchTextState: String = sharedViewModel.searchTextState

    val snackbarHostState = remember { SnackbarHostState() }

    DisplaySnackBar(
        snackbarHostState = snackbarHostState,
        onComplete = { sharedViewModel.updateAction(it) },
        onUndoClicked = {sharedViewModel.updateAction(it)},
        taskTitle = sharedViewModel.title,
        action = action
    )

    Scaffold(
        topBar = {
            ListAppBar(
                allTasks = allTasks,
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = {
            ListContent(
                allTasks = allTasks,
                searchedTasks = searchedTasks,
                lowPriorityTasks = lowPriorityTasks,
                highPriorityTasks = highPriorityTasks,
                sortState = sortState,
                searchAppBarState = searchAppBarState,
                onSwipeToDelete = { action, toDoTask ->
                    sharedViewModel.updateAction(newAction = action)
                    sharedViewModel.updateTaskFields(selectedTask = toDoTask)
                    snackbarHostState.currentSnackbarData?.dismiss()
                },
                navigateToTaskScreen = navigateToTaskScreen,
                modifier = Modifier.padding(top = it.calculateTopPadding())
            )
        },
        floatingActionButton = {
            ListFab(onFabClicked = navigateToTaskScreen)
        }
    )
}

@Composable
fun ListFab(onFabClicked: (taskId: Int) -> Unit){

    FloatingActionButton(onClick = { onFabClicked(-1) }, containerColor = MaterialTheme.colorScheme.primary) {
        Icon(imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_button),
            tint = Color.Red)
    }
}

@Composable
fun DisplaySnackBar(
    snackbarHostState: SnackbarHostState,
    onComplete: (Action) -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle: String,
    action: Action
){
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action)
    {
        if (action != Action.NO_ACTION){
            scope.launch {
                val snackBarResult = snackbarHostState.showSnackbar(
                    message = setMessage(action = action, taskTitle = taskTitle),
                    actionLabel = setActionLabel(action),
                    duration = SnackbarDuration.Short
                )
                undoDeletedTask(
                    action = action,
                    snackbarResult = snackBarResult,
                    onUndoClicked = onUndoClicked
                )
            }
            onComplete(Action.NO_ACTION)
        }
    }
}

private fun setMessage(action: Action,taskTitle: String): String{
    return when(action){
        Action.DELETE_ALL -> "All tasks removed"
        else -> "${action.name}: $taskTitle"
    }
}

private fun setActionLabel(action: Action): String{
    return if (action.name == "DELETE"){
        "UNDO"
    } else {
        "OK"
    }
}

private fun undoDeletedTask(
    action: Action,
    snackbarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit
){
    if (snackbarResult == SnackbarResult.ActionPerformed && action == Action.DELETE){
        onUndoClicked(Action.UNDO)
    }
}