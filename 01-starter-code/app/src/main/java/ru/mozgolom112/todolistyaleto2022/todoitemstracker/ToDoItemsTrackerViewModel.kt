package ru.mozgolom112.todolistyaleto2022.todoitemstracker

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import ru.mozgolom112.todolistyaleto2022.database.ToDoItem
import ru.mozgolom112.todolistyaleto2022.database.ToDoListDatabaseDao
import java.util.*

val EMPTY_TODO_ITEM = null

class ToDoItemsTrackerViewModel(
    val database: ToDoListDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val _toDoItems = database.getAllItems()
    val toDoItems: LiveData<List<ToDoItem>?>
        get() = _toDoItems

    //Navigation

    private val _navigateToDetails = MutableLiveData<Boolean?>(false)
    val navigateToDetails: LiveData<Boolean?>
        get() = _navigateToDetails

    private var _itemToDetails: ToDoItem? = EMPTY_TODO_ITEM
    val itemToDetails: ToDoItem?
        get() = _itemToDetails

    fun navigateToItemDetails(selectedItem: ToDoItem){
        _itemToDetails = selectedItem
        _navigateToDetails.value = true
    }

    fun onCreateNewItem() {
        _itemToDetails = EMPTY_TODO_ITEM
        _navigateToDetails.value = true
    }

    fun doneNavigation() {
        _itemToDetails = null
        _navigateToDetails.value = null
    }
}