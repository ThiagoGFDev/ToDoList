package com.example.todolist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ToDoEntity::class],
    version = 1,
)
abstract class ToDoDatabase : RoomDatabase() {

    abstract val dao: ToDoDao
}

object ToDoDatabaseProvider {

    @Volatile
    private var INSTANCE: ToDoDatabase? = null

    fun provide(context: Context): ToDoDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                ToDoDatabase::class.java,
                "todo_app"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}