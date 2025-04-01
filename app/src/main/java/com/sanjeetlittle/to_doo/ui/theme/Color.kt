package com.sanjeetlittle.to_doo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple700 = Color(0xFF3700B3)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val taskItemBackgroundColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.DarkGray else Color.White

val taskItemTextColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color.White else Color.Black

val LowPriorityColor = Color(0xFF00C980)
val MediumPriorityColor = Color(0xFFFFC114)
val HighPriorityColor = Color(0xFFFF4646)
val NonePriorityColor = Color(0xFFFFFFFF)