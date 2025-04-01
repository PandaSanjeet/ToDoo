package com.sanjeetlittle.to_doo.ui.screens.task

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.sanjeetlittle.to_doo.data.models.Priority
import com.sanjeetlittle.to_doo.data.models.ToDoTask
import com.sanjeetlittle.to_doo.util.Action
import com.sanjeetlittle.to_doo.viewModels.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    selectedTask: ToDoTask?,
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (Action) -> Unit
){

    val title: String = sharedViewModel.title
    val description: String = sharedViewModel.description
    val priority: Priority = sharedViewModel.priority

    val context = LocalContext.current

    BackHandler {
        navigateToListScreen(Action.NO_ACTION)
    }

    Scaffold(
        topBar = {
                 TaskAppBar(
                     selectedTask
                 ) {
                     if (it == Action.NO_ACTION) {
                         navigateToListScreen(it)
                     } else {
                         if (sharedViewModel.validateFields()) {
                             navigateToListScreen(it)
                         } else {
                             displayToast(context)
                         }
                     }
                 }
        },
        content = {
            TaskContent(
                title = title,
                onTitleChange = {sharedViewModel.updateTitle(it)},
                description = description,
                onDescriptionChange = {sharedViewModel.updateDescription(it)},
                priority = priority,
                onPrioritySelected = {sharedViewModel.updatePriority(it)},
                modifier = Modifier.padding(top = it.calculateTopPadding())
            )
        }
    )
}

fun displayToast(context: Context) {
    Toast.makeText(context,"Fields Empty",Toast.LENGTH_SHORT).show()
}
