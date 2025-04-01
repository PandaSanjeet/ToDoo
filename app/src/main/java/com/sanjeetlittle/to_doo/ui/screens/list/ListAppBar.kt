package com.sanjeetlittle.to_doo.ui.screens.list

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanjeetlittle.to_doo.R
import com.sanjeetlittle.to_doo.components.DisplayAlertDialog
import com.sanjeetlittle.to_doo.components.PriorityItem
import com.sanjeetlittle.to_doo.data.models.Priority
import com.sanjeetlittle.to_doo.data.models.ToDoTask
import com.sanjeetlittle.to_doo.util.Action
import com.sanjeetlittle.to_doo.util.RequestState
import com.sanjeetlittle.to_doo.util.SearchAppBarState
import com.sanjeetlittle.to_doo.viewModels.SharedViewModel

@Composable
fun ListAppBar(
    allTasks: RequestState<List<ToDoTask>>,
    sharedViewModel: SharedViewModel,
    searchAppBarState: SearchAppBarState,
    searchTextState: String
){
    when(searchAppBarState){
        SearchAppBarState.CLOSED -> {
            if (allTasks is RequestState.Success){
                DefaultListAppBar(
                    tasks = allTasks.data,
                    onSearchClicked = {sharedViewModel.updateAppBarState(SearchAppBarState.OPENED)},
                    onSortClicked = {sharedViewModel.persistSortState(it)},
                    onDeleteAllConfirmed = {
                        sharedViewModel.updateAction(Action.DELETE_ALL)
                    }
                )
            }
        }
        else -> {
            SearchAppBar(
                text = searchTextState,
                onTextChange = {sharedViewModel.updateSearchText(it)},
                onCloseClicked = {
                    sharedViewModel.updateAppBarState(SearchAppBarState.CLOSED)
                    sharedViewModel.updateSearchText("")
                },
                onSearchClicked = {
                    sharedViewModel.searchDatabase(searchQuery = it)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultListAppBar(
    tasks: List<ToDoTask>,
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmed: () -> Unit
){
    TopAppBar(
        title = { Text(text = stringResource(R.string.tasks), color = MaterialTheme.colorScheme.surfaceBright) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        actions = {
            ListAppBarActions (
                tasks = tasks,
                onSearchClicked = onSearchClicked,
                onSortClicked = onSortClicked,
                onDeleteAllConfirmed = onDeleteAllConfirmed
            )
        }
    )
}
@Composable
fun ListAppBarActions(
    tasks: List<ToDoTask>,
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmed: () -> Unit
){
    var openDialog by remember { mutableStateOf(false) }

    DisplayAlertDialog(
        title = stringResource(id = R.string.delete_all_tasks),
        message = stringResource(id = R.string.delete_all_tasks_confirmation),
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onYesClicked ={ onDeleteAllConfirmed() }
    )

    SearchAction(onSearchClicked = onSearchClicked)
    SortAction(onSortClicked = onSortClicked)
    DeleteAllAction(tasks = tasks, onDeleteAllConfirmed = { openDialog = true })
}

@Composable
fun SearchAction(onSearchClicked: () -> Unit){
    IconButton(onClick = { onSearchClicked() }) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(R.string.search_tasks),
            tint = MaterialTheme.colorScheme.surfaceBright

        )
    }
}

@Composable
fun SortAction(onSortClicked: (Priority) -> Unit){
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_filter_list),
            contentDescription = stringResource(R.string.sort_tasks),
            tint = MaterialTheme.colorScheme.surfaceBright
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Priority.values().slice(setOf(0,2,3)).forEach {
                DropdownMenuItem(text = { PriorityItem(priority = it) }, onClick = {
                    expanded = false
                    onSortClicked(it) })
            }
        }
    }
}

@Composable
fun DeleteAllAction(tasks: List<ToDoTask>, onDeleteAllConfirmed: () -> Unit){
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current

    IconButton(onClick = { expanded = true }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_more_vert),
            contentDescription = stringResource(R.string.delete_all_taskss),
            tint = MaterialTheme.colorScheme.surfaceBright
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(
                    R.string.delete_all
                )
            )}, onClick = {
                expanded = false
                if (tasks.isEmpty()){
                    Toast.makeText(context,"No tasks to delete", Toast.LENGTH_SHORT).show()
                } else {
                    onDeleteAllConfirmed()
                }
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.primary
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.50f),
            value = text,
            onValueChange = {
                            onTextChange(it)
            },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(0.9f),
                    text = stringResource(R.string.search),
                    color = MaterialTheme.colorScheme.primary
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                fontWeight = FontWeight.Normal
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier.alpha(0.38f),
                    onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(R.string.search_icon),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            },
            trailingIcon = {
                IconButton(onClick = {
                    if (text.isNotEmpty()){
                        onTextChange("")
                    } else {
                        onCloseClicked()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.close_icon),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {onSearchClicked(text)}
            ),
            colors = TextFieldDefaults.colors(
                cursorColor = MaterialTheme.colorScheme.primaryContainer,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
@Preview
fun SearchAppBarPreview(){
    SearchAppBar(text = "Search", onTextChange = {}, onCloseClicked = {}, onSearchClicked = {})
}