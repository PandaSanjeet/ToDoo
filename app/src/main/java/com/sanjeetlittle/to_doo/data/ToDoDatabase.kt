package com.sanjeetlittle.to_doo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sanjeetlittle.to_doo.data.models.ToDoTask

@Database(entities = [ToDoTask::class], version = 1, exportSchema = false)
abstract class ToDoDatabase: RoomDatabase() {
    abstract fun toDoDao(): ToDoDao
}