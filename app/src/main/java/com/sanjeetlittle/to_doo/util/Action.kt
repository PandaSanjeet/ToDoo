package com.sanjeetlittle.to_doo.util

enum class Action {
    ADD,
    UPDATE,
    DELETE,
    DELETE_ALL,
    UNDO,
    NO_ACTION
}

fun String?.toAction(): Action{
    return if (this.isNullOrEmpty()) Action.NO_ACTION else Action.valueOf(this)
}