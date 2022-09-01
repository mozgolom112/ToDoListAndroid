package ru.mozgolom112.todolistyaleto2022.todoitemdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.mozgolom112.todolistyaleto2022.database.ToDoItem
import ru.mozgolom112.todolistyaleto2022.database.ToDoListDatabaseDao
import ru.mozgolom112.todolistyaleto2022.todoitemstracker.EMPTY_ID

class ToDoItemDetailViewModel(
    val toDoItemId: String,
    private val database: ToDoListDatabaseDao
): ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    val _toDoItemIdLiveData = MutableLiveData<String>(toDoItemId)

    fun onSaveItemClick() {
        Log.i("ToDoItemDetailViewModel", "onSaveNewItemClicked")
        uiScope.launch {
            Log.i("ToDoItemDetailViewModel", "toDoItem is $toDoItemId")
            if (_toDoItemIdLiveData.value == EMPTY_ID){
                Log.i("ToDoItemDetailViewModel", "onSaveNewItemClicked")
                val newToDoItem =ToDoItem(description = "Test")
                insert(newToDoItem)
                Log.i("ToDoItemDetailViewModel", "onSaveNewItemClick saved newToDoItem ${newToDoItem.id}")
                _toDoItemIdLiveData.value = newToDoItem.id
                //_navigateToTracker.value = true
            } else{
                //TODO("UpdateInfo")
                _navigateToTracker.value = true
            }

        }
    }

    private suspend fun insert(toDoItem: ToDoItem){
        withContext(Dispatchers.IO){
            database.insertToDoItem(toDoItem)
        }
    }

    private val _navigateToTracker = MutableLiveData<Boolean?>()
    val navigateToTracker: LiveData<Boolean?>
        get() = _navigateToTracker

    fun doneNavigating() {
        _navigateToTracker.value = null
    }

}