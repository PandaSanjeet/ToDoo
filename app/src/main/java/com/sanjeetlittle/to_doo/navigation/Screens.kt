package com.sanjeetlittle.to_doo.navigation

import androidx.navigation.NavHostController
import com.sanjeetlittle.to_doo.util.Action
import com.sanjeetlittle.to_doo.util.Constants.LIST_SCREEN

class Screens(navController: NavHostController) {

    val list: (Action) -> Unit = {action ->
        navController.navigate(route = "list/${action.name}"){
            popUpTo(LIST_SCREEN){inclusive = true}
        }
    }

    val task: (Int) -> Unit = { taskId ->
        navController.navigate("task/$taskId")
    }
}