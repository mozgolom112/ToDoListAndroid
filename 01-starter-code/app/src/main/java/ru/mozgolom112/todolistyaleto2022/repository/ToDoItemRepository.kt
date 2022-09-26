package ru.mozgolom112.todolistyaleto2022.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.mozgolom112.todolistyaleto2022.database.ToDoListDatabase
import ru.mozgolom112.todolistyaleto2022.domain.ToDoItem
import ru.mozgolom112.todolistyaleto2022.util.asDomainModel

class ToDoItemRepository(private val database: ToDoListDatabase) {

    val toDoItems: LiveData<List<ToDoItem>> =
        Transformations.map(database.toDoListDatabaseDao.getAllItems()){
            Log.i("ToDoItemRepository", "Repo was updated")
            it?.asDomainModel()
        }
}