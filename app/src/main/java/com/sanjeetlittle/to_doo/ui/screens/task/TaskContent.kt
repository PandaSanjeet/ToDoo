package com.sanjeetlittle.to_doo.ui.screens.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sanjeetlittle.to_doo.components.PriorityDropDown
import com.sanjeetlittle.to_doo.data.models.Priority
import com.sanjeetlittle.to_doo.ui.theme.MEDIUM_PADDING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskContent(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit,
    modifier: Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = MEDIUM_PADDING)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = {onTitleChange(it)},
            label = { Text(text = "Title" ) },
            textStyle = MaterialTheme.typography.bodySmall,
            singleLine = true
        )
        HorizontalDivider(
            modifier = Modifier.height(MEDIUM_PADDING),
            color = MaterialTheme.colorScheme.background
        )

        PriorityDropDown(priority = priority, onPrioritySelected = onPrioritySelected)

        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = description,
            onValueChange = {onDescriptionChange(it)},
            label = { Text(text = "Description")},
            textStyle = MaterialTheme.typography.bodySmall)
    }
}

@Composable
@Preview
private fun TaskContentPreview(){
    TaskContent(
        title = "",
        onTitleChange = {},
        description = "",
        onDescriptionChange = {},
        priority = Priority.MEDIUM,
        onPrioritySelected = {},
        modifier = Modifier
    )
}
