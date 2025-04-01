package com.sanjeetlittle.to_doo.ui.screens.task

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.sanjeetlittle.to_doo.R
import com.sanjeetlittle.to_doo.components.DisplayAlertDialog
import com.sanjeetlittle.to_doo.data.models.ToDoTask
import com.sanjeetlittle.to_doo.util.Action

@Composable
fun TaskAppBar(selectedTask: ToDoTask?, navigateToListScreen: (Action) -> Unit){
    if (selectedTask == null){
        NewTaskAppBar(navigateToListScreen = navigateToListScreen)
    } else{
        ExistingTaskAppBar(selectedTask = selectedTask, navigateToListScreen = navigateToListScreen)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskAppBar(navigateToListScreen: (Action) -> Unit){
    TopAppBar(
        title = {
            Text(
                text = "Add Task",
                color = MaterialTheme.colorScheme.surfaceBright
            )
        }, 
        navigationIcon = {
             BackAction(onBlackClicked = navigateToListScreen)
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        actions = {
            AddAction(onAddClicked = navigateToListScreen)
        }
    )
}

@Composable
fun BackAction(onBlackClicked: (Action) -> Unit){
    IconButton(onClick = {onBlackClicked(Action.NO_ACTION)}) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back arrow",
            tint = MaterialTheme.colorScheme.surfaceBright
        )
    }
}

@Composable
fun AddAction(onAddClicked: (Action) -> Unit){
    IconButton(onClick = {onAddClicked(Action.ADD)}) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "Add task",
            tint = MaterialTheme.colorScheme.surfaceBright
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExistingTaskAppBar(
    selectedTask: ToDoTask,
    navigateToListScreen: (Action) -> Unit
){
    TopAppBar(
        title = {
            Text(
                text = selectedTask.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.surfaceBright
            )
        },
        navigationIcon = {
            CloseAction(onCloseClicked = navigateToListScreen)
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        actions = {
            ExistingTaskAppBarActions(selectedTask = selectedTask, navigateToListScreen = navigateToListScreen)
        }
    )
}

@Composable
fun CloseAction(onCloseClicked: (Action) -> Unit){
    IconButton(onClick = {onCloseClicked(Action.NO_ACTION)}) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "close",
            tint = MaterialTheme.colorScheme.surfaceBright
        )
    }
}

@Composable
fun ExistingTaskAppBarActions(
    selectedTask: ToDoTask,
    navigateToListScreen: (Action) -> Unit
){
    var openDialog by remember { mutableStateOf(false) }

    DisplayAlertDialog(
        title = stringResource(id = R.string.delete_task, selectedTask.title),
        message = stringResource(id = R.string.delete_task_confirmation, selectedTask.title),
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onYesClicked = {navigateToListScreen(Action.DELETE)}
    ) 

    DeleteAction(onDeleteClicked = { openDialog = true })
    UpdateAction(onUpdateClicked = navigateToListScreen)
}

@Composable
fun DeleteAction(onDeleteClicked: () -> Unit){
    IconButton(onClick = {onDeleteClicked()}) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = "delete icon",
            tint = MaterialTheme.colorScheme.surfaceBright
        )
    }
}

@Composable
fun UpdateAction(onUpdateClicked: (Action) -> Unit){
    IconButton(onClick = {onUpdateClicked(Action.UPDATE)}) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "update icon",
            tint = MaterialTheme.colorScheme.surfaceBright
        )
    }
}
