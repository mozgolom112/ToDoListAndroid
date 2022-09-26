package ru.mozgolom112.todolistyaleto2022.ui.todoitemstracker.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mozgolom112.todolistyaleto2022.database.ToDoListDatabaseDao

class ToDoItemsTrackerViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ToDoItemsTrackerViewModel::class.java)) {
            return ToDoItemsTrackerViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}