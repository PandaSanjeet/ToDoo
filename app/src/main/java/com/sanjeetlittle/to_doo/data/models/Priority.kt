package com.sanjeetlittle.to_doo.data.models

import com.sanjeetlittle.to_doo.ui.theme.HighPriorityColor
import com.sanjeetlittle.to_doo.ui.theme.LowPriorityColor
import com.sanjeetlittle.to_doo.ui.theme.MediumPriorityColor
import com.sanjeetlittle.to_doo.ui.theme.NonePriorityColor

enum class Priority(val color: androidx.compose.ui.graphics.Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor)
}