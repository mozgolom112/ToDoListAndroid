package ru.mozgolom112.todolistyaleto2022.todoitemstracker

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.mozgolom112.todolistyaleto2022.database.ToDoItem
import ru.mozgolom112.todolistyaleto2022.database.ToDoListDatabaseDao
import java.util.*

class ToDoItemsTrackerViewModel(
    val database: ToDoListDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val _toDoItems = MutableLiveData<List<ToDoItem?>>()
    val toDoItems: LiveData<List<ToDoItem?>>
        get() = _toDoItems

    private val _navigateToDetails = MutableLiveData<ToDoItem?>()
    val navigateToDetails: LiveData<ToDoItem?>
        get() = _navigateToDetails

    fun onItemClick(){
        Log.i("ToDoItemsTrackerViewModel","onItemClick")
    }

    private val _navigateToCreateNewToDoItem = MutableLiveData<Boolean>(false)
    val navigateToCreateNewToDoItem: LiveData<Boolean>
        get() = _navigateToCreateNewToDoItem

    fun onCreateNewItem() {
        _navigateToCreateNewToDoItem.value = true
    }

    fun doneNavigation() {
        _navigateToCreateNewToDoItem.value = false
    }
}