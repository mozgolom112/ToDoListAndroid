package ru.mozgolom112.todolistyaleto2022.todoitemdetail

import androidx.lifecycle.ViewModel
import ru.mozgolom112.todolistyaleto2022.database.ToDoItem
import ru.mozgolom112.todolistyaleto2022.database.ToDoListDatabaseDao

class ToDoItemDetailViewModel(
    val toDoItemId: String?,
    private val database: ToDoListDatabaseDao
): ViewModel() {


}