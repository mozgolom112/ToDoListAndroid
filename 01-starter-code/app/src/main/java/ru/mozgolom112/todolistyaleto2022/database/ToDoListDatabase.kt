package ru.mozgolom112.todolistyaleto2022.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ToDoItemDatabase::class], version = 1, exportSchema = false)
abstract class ToDoListDatabase : RoomDatabase() {
    abstract val toDoListDatabaseDao: ToDoListDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: ToDoListDatabase? = null

        fun getInstance(context: Context): ToDoListDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ToDoListDatabase::class.java,
                        "to_do_item_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}