package ru.mozgolom112.todolistyaleto2022.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ToDoListDatabaseDao {
    //Вставить запись
    @Insert
    fun insertToDoItem(toDoItem: ToDoItem)

    //Обновить запись
    @Update
    fun updateToDoItem(toDoItem: ToDoItem)

    //Удалить записи по списку
    @Delete
    fun deleteToDoItems(toDoItems: List<ToDoItem>)

    //Удалить по id записи
    @Query("DELETE FROM to_do_items WHERE to_do_item_id = :id")
    fun deleteToDoItemByID(id: String)

    //Удалить все записи
    @Query("DELETE FROM to_do_items")
    fun cleanToDoItems()

    //Получить весь список
    @Query("SELECT * FROM to_do_items ORDER BY date_create")
    fun getAllItems(): LiveData<List<ToDoItem>?>
}