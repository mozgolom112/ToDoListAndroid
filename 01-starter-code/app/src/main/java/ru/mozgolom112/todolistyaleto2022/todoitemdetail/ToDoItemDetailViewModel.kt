package ru.mozgolom112.todolistyaleto2022.todoitemdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ru.mozgolom112.todolistyaleto2022.database.ToDoItem
import ru.mozgolom112.todolistyaleto2022.database.ToDoListDatabaseDao
import ru.mozgolom112.todolistyaleto2022.todoitemstracker.EMPTY_TODO_ITEM

const val EMPTY_STRING = ""

enum class DetailErrors(val text: String) {
    EMPTY_ERROR(""),
    EMPTY_DESCRIPTION_ERROR("Пожалуйста, добавьте текст задачи. Он не может быть пустым"),
    INSERT_UNIQUE_ITEM_ERROR("Упс! Дело с таким ID уже существует. Явно произошла какая-то ошибка. Вы все равно хотите создать дело? Да | Нет"),
    UPDATE_NON_EXISTENT_ITEM("В базе данных дела с таким ID больше не существует. Возможно он уже был удален. Хотите создать новое дело? Да | Нет")
}

class ToDoItemDetailViewModel(
    selectedDoItem: ToDoItem?,
    private val database: ToDoListDatabaseDao
) : ViewModel() {


    private var _currentToDoItem = MutableLiveData<ToDoItem?>(selectedDoItem)
    val currentToDoItem: LiveData<ToDoItem?>
        get() = _currentToDoItem

    //Поля UI слоя
    private var _description: String = ""
    private var _priority: Int = 0
    private val _dateDeadline = MutableLiveData<Long>()
    val dateDeadline: LiveData<Long>
        get() = _dateDeadline

    fun onSaveItemClick() {
        getUIFields()
        viewModelScope.launch {
            if (_currentToDoItem.value == EMPTY_TODO_ITEM) {
                //Insert item
                Log.i("DetailViewModel", "Insert branch was called")
                if (isDescriptionEmpty()) return@launch
                val newToDoItem = ToDoItem(_description, _priority, _dateDeadline?.value ?: -1)
                if (insertItem(newToDoItem)) return@launch
            } else {
                Log.i("DetailViewModel", "Update branch was called")
                //Update item
                //Тут важен порядок проверки. Сначала проверим есть ли такое дело в Room
                //Далее необходимо ставить isInfoTheSame в проверке, так как при
                //положительном ответе мы просто навигируемся обратно к Трекер листу
                if (!isExistInRoom() || isInfoTheSame() || isDescriptionEmpty()) return@launch
                setNewValues()
                update(currentToDoItem.value)
            }
            _navigateToTracker.value = true
        }
    }

    private fun getUIFields() {
        Log.i("ToDoItemDetailViewModel", "toDoItem is ${_currentToDoItem.value?.id ?: "empty"}")
        _getFieldsFlag.value = true
        Log.i(
            "ToDoItemDetailViewModel",
            "toDoItem is $_description ${_dateDeadline.value} $_priority"
        )
        _getFieldsFlag.value = false
    }

    //Проверка на пустое значение поля description
    private fun isDescriptionEmpty(): Boolean {
        if (_description == EMPTY_STRING) {
            _errorTextDescription.value = DetailErrors.EMPTY_DESCRIPTION_ERROR
            return true
        }
        return false
    }

    //Проверка изменяемых полей
    private fun isInfoTheSame(): Boolean =
        _currentToDoItem.value?.let {
            if (_description == it.description && _priority == it.priority && _dateDeadline.value == it.dateDeadline) {
                _navigateToTracker.value = true
                return true//Навигируемся обратно
            }
            return false//Информация разлчна, необходимо сохранить новые данные
        } ?: false//Объект был null. Не использовать при создании элемента

    private suspend fun isExistInRoom(): Boolean {
        _currentToDoItem.value?.let {
            if (getToDoItem(it.id) == null) {
                Log.i("DetailViewModel", "errorTextDescription")
                _errorTextDescription.value = DetailErrors.UPDATE_NON_EXISTENT_ITEM
                return false//Объекта не существует в Room
            }
            return true //Объект существует
        } ?: return false //Объект null
    }

    private fun setNewValues() {
        _currentToDoItem.value?.let {
            it.description = _description
            it.priority = _priority
            it.dateDeadline = _dateDeadline.value ?: -1L
            it.dateModified = System.currentTimeMillis()
        }
    }

    private suspend fun ToDoItemDetailViewModel.insertItem(
        newToDoItem: ToDoItem
    ): Boolean {
        try {
            insert(newToDoItem)
        } catch (e: Exception) {
            _errorTextDescription.value = DetailErrors.INSERT_UNIQUE_ITEM_ERROR
            return true
        }
        return false
    }

    //Database

    private suspend fun insert(item: ToDoItem?) = withContext(Dispatchers.IO) {
        item?.let { database.insertToDoItem(it) } ?: throw Error("Попытка добавить пустой элемент")
    }

    private suspend fun update(item: ToDoItem?) = withContext(Dispatchers.IO) {
        try {
            item?.let { database.updateToDoItem(it) }
                ?: throw Error("Попытка обновить пустой элемент")
        } catch (e: Exception) {
            Log.i("DetailViewModel", "Ошибка обновления $e")
        }
    }

    private suspend fun getToDoItem(toDoItemId: String): ToDoItem? = withContext(Dispatchers.IO) {
        database.getToDoItemByID(toDoItemId)
    }

    //Click

    //Navigation

    private val _navigateToTracker = MutableLiveData<Boolean?>()
    val navigateToTracker: LiveData<Boolean?>
        get() = _navigateToTracker

    fun doneNavigating() {
        _navigateToTracker.value = null
    }

    //Support func
    private val _getFieldsFlag = MutableLiveData<Boolean>(false)
    val getFieldsFlag: LiveData<Boolean>
        get() = _getFieldsFlag

    fun getToDoItemFields(description: String, priority: Int) {
        _description = description
        _priority = priority
    }

    fun saveDeadlineDate(timestamp: Long) {
        _dateDeadline.value = timestamp//Date(year, month, day).time
        Log.i("Calendar", "Timestamp in ViewModel ${_dateDeadline.value}")
    }

    //Errors
    private val _errorTextDescription = MutableLiveData<DetailErrors>(DetailErrors.EMPTY_ERROR)
    val errorTextDescription: LiveData<DetailErrors>
        get() = _errorTextDescription

    fun doneSnackBarEvent() {
        _errorTextDescription.value = DetailErrors.EMPTY_ERROR
    }

    fun forceRemoveItemID() {
        //Мы автоматически теряем все данные, которые пришли нам из Tracker
        //Т.е. мы теряем ID, status, время создания и время изменения
        _currentToDoItem.value = EMPTY_TODO_ITEM
    }
}
