package ru.mozgolom112.todolistyaleto2022.ui.todoitemdetail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mozgolom112.todolistyaleto2022.database.ToDoListDatabaseDao
import ru.mozgolom112.todolistyaleto2022.domain.ToDoItem

class ToDoItemDetailViewModelFactory(
    private val selectedToDoItem: ToDoItem?,
    private val dataSource: ToDoListDatabaseDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ToDoItemDetailViewModel::class.java)){
            return ToDoItemDetailViewModel(selectedToDoItem, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}