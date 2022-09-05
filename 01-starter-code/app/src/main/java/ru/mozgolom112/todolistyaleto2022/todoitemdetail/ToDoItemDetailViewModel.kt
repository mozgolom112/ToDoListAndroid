package ru.mozgolom112.todolistyaleto2022.todoitemdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.mozgolom112.todolistyaleto2022.database.ToDoItem
import ru.mozgolom112.todolistyaleto2022.database.ToDoListDatabaseDao
import ru.mozgolom112.todolistyaleto2022.todoitemstracker.EMPTY_ID
import java.lang.Exception

class ToDoItemDetailViewModel(
    val toDoItemId: String,
    private val database: ToDoListDatabaseDao
): ViewModel() {

    //Поля UI слоя
    private var _description: String = ""
    private var _priority: Int = 0
    private var _dateDeadline: Long = -1L


    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    val _toDoItemIdLiveData = MutableLiveData<String>(toDoItemId)

    init {
        Log.i("ToDoItemDetailViewModel", "init")
        if (toDoItemId != EMPTY_ID){
            uiScope.launch {
                //TODO(Добавить флаг waitInfo с имитацией прихода данных, и сделать заглушки загрузки)
                try {
                    _currentToDoItem.value = getToDoItem(toDoItemId)
                }catch (ex: Exception){
                    throw(ex)
                }

                //TODO(Проверить обновления данных с сервера в live режиме)
            }
        }
    }

    private var _currentToDoItem = MutableLiveData<ToDoItem?>(null)
    val currentToDoItem: LiveData<ToDoItem?>
        get() = _currentToDoItem

    private val _getFieldsFlag = MutableLiveData<Boolean>(false)
    val getFieldsFlag: LiveData<Boolean>
        get() = _getFieldsFlag

    fun getToDoItemFields(description: String, priority: Int, dateDeadline: Long){
        _description = description
        _priority = priority
        _dateDeadline = dateDeadline
    }
    private val _emptyDescriptionError = MutableLiveData<Boolean>(false)
    val emptyDescriptionError: LiveData<Boolean>
        get() = _emptyDescriptionError

    fun onSaveItemClick() {
        Log.i("ToDoItemDetailViewModel", "onSaveNewItemClicked")
        uiScope.launch {
            Log.i("ToDoItemDetailViewModel", "toDoItem is $toDoItemId")
            _getFieldsFlag.value = true
            Log.i("ToDoItemDetailViewModel", "toDoItem is $_description $_dateDeadline $_priority")
            _getFieldsFlag.value = false
            if (_toDoItemIdLiveData.value == EMPTY_ID){
                Log.i("ToDoItemDetailViewModel", "onSaveNewItemClicked")
                if (_description == ""){
                    _emptyDescriptionError.value=true
                    return@launch
                }

                val newToDoItem = ToDoItem(id = "1", description = _description, priority = _priority, dateDeadline = _dateDeadline)
                insert(newToDoItem)
                Log.i("ToDoItemDetailViewModel", "onSaveNewItemClick saved newToDoItem ${newToDoItem.id}")
                _toDoItemIdLiveData.value = newToDoItem.id
                _navigateToTracker.value = true
            } else{
                //TODO("UpdateInfo")
                if (_description == ""){
                    _emptyDescriptionError.value=true
                    return@launch
                }
                if (infoSame()){
                    //Просто навигируемся, но в целом можно еще всплывающее окно показать
                    _navigateToTracker.value = true
                } else{
                    setNewValues()
                    currentToDoItem.value?.let {
                        update(it)
                    }
                    _navigateToTracker.value = true
                }

            }

        }
    }

    private fun setNewValues() {
        _currentToDoItem.value?.let {
            it.description = _description
            it.priority = _priority
            it.dateDeadline = _dateDeadline
            it.dateModified = System.currentTimeMillis()
        }
    }

    //Проверка изменяемых полей
    private fun infoSame(): Boolean = _currentToDoItem.value?.let {
        _description == it.description && _priority == it.priority && _dateDeadline == it.dateDeadline
    }?: false

    private suspend fun insert(toDoItem: ToDoItem){
        withContext(Dispatchers.IO){
            database.insertToDoItem(toDoItem)
        }
    }

    private suspend fun update(toDoItem: ToDoItem){
        withContext(Dispatchers.IO){
            database.updateToDoItem(toDoItem)
        }
    }

    private suspend fun getToDoItem(toDoItemId: String): ToDoItem?{
        return withContext(Dispatchers.IO){
            database.getToDoItemByID(toDoItemId)
        }
    }

    private val _navigateToTracker = MutableLiveData<Boolean?>()
    val navigateToTracker: LiveData<Boolean?>
        get() = _navigateToTracker

    fun doneNavigating() {
        _navigateToTracker.value = null
    }

    fun doneSnackBarEvent() {
        _emptyDescriptionError.value=false
    }


}
