package com.sanjeetlittle.to_doo.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.sanjeetlittle.to_doo.R

@Composable
fun DisplayAlertDialog(
    title: String,
    message: String,
    openDialog: Boolean,
    closeDialog: () -> Unit,
    onYesClicked: () -> Unit
){
    if (openDialog){
        AlertDialog(
            title = {
                Text(text = title, fontSize = MaterialTheme.typography.titleMedium.fontSize, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(text = message, fontSize = MaterialTheme.typography.titleMedium.fontSize, fontWeight = FontWeight.Normal)
            },
            onDismissRequest = { closeDialog() },
            dismissButton = { OutlinedButton(onClick = { closeDialog() }) {Text(text = stringResource(R.string.no)) } },
            confirmButton = {
                Button(
                    onClick = {
                        onYesClicked()
                        closeDialog()
                    }
                ) {
                    Text(text = stringResource(R.string.yes))
                }
            }
        )
    }
}