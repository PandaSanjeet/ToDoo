package com.sanjeetlittle.to_doo.dependencyInjection

import android.content.Context
import androidx.room.Room
import com.sanjeetlittle.to_doo.data.ToDoDao
import com.sanjeetlittle.to_doo.data.ToDoDatabase
import com.sanjeetlittle.to_doo.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ToDoDatabase = Room.databaseBuilder(context,ToDoDatabase::class.java,DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideDao(toDoDatabase: ToDoDatabase):ToDoDao = toDoDatabase.toDoDao()
}