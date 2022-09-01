package ru.mozgolom112.todolistyaleto2022.todoitemdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mozgolom112.todolistyaleto2022.database.ToDoListDatabaseDao

class ToDoItemDetailViewModelFactory(
    private val toDoItemId: String?,
    private val dataSource: ToDoListDatabaseDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ToDoItemDetailViewModel::class.java)){
            return ToDoItemDetailViewModel(toDoItemId, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}