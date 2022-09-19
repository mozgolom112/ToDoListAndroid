package ru.mozgolom112.todolistyaleto2022.ui.todoitemstracker.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.mozgolom112.todolistyaleto2022.database.ToDoItem
import ru.mozgolom112.todolistyaleto2022.database.ToDoListDatabaseDao
import java.lang.Exception

val EMPTY_TODO_ITEM = null

class ToDoItemsTrackerViewModel(
    val database: ToDoListDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

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

    fun navigateToItemDetails(selectedItem: ToDoItem) {
        _itemToDetails = selectedItem
        _navigateToDetails.value = true
    }

    //Clicks

    fun onCreateNewItem() {
        _itemToDetails = EMPTY_TODO_ITEM
        _navigateToDetails.value = true
    }

    fun doneNavigation() {
        _itemToDetails = null
        _navigateToDetails.value = null
    }

    //Обработка измений состояния дела
    fun changeItemState(selectedItem: ToDoItem) {
        viewModelScope.launch {
            try {
                updateItemState(selectedItem)
            } catch (e: Exception) {
                Log.e("ToDoItemsTrackerViewModel", "Ошибка при обновлении состояния item $e")
            }
        }
    }

    private suspend fun updateItemState(item: ToDoItem) = withContext(Dispatchers.IO) {
        item.isCompleted = !item.isCompleted //меняем на противоположный
        item.dateModified = System.currentTimeMillis()
        database.updateToDoItem(item)
        Log.i("ToDoItemsTrackerViewModel", "Item $item обновил состояние на ${item.isCompleted}")
    }
}