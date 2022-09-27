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
import ru.mozgolom112.todolistyaleto2022.database.ToDoListDatabase
import ru.mozgolom112.todolistyaleto2022.domain.ToDoItem
import ru.mozgolom112.todolistyaleto2022.repository.ToDoItemRepository
import ru.mozgolom112.todolistyaleto2022.util.asDatabaseModel

val EMPTY_TODO_ITEM = null

class ToDoItemsTrackerViewModel(
    application: Application
) : AndroidViewModel(application) { //Оставил пока в качестве примера, но лучше использовать просто ViewModel

    private val db by lazy(Dispatchers.IO) { ToDoListDatabase.getInstance(application.applicationContext) }
    private val repository by lazy(Dispatchers.IO) { ToDoItemRepository(db) }

    val toDoItemsDatabase = repository.toDoItems

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
        val itemClone = item.copy(isCompleted = !item.isCompleted, dateModified = System.currentTimeMillis())
        db.toDoListDatabaseDao.updateToDoItem(itemClone.asDatabaseModel())
        Log.i("ToDoItemsTrackerViewModel", "Item $item обновил состояние на ${item.isCompleted}")
    }
}