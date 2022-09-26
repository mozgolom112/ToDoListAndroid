package ru.mozgolom112.todolistyaleto2022.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ToDoListDatabaseDao {
    //Вставить запись
    @Insert
    fun insertToDoItem(toDoItemDatabase: ToDoItemDatabase)

    //Обновить запись
    @Update
    fun updateToDoItem(toDoItemDatabase: ToDoItemDatabase)

    //Удалить записи по списку
    @Delete
    fun deleteToDoItems(toDoItemDatabases: List<ToDoItemDatabase>)

    //Удалить по id записи
    @Query("DELETE FROM to_do_items WHERE to_do_item_id = :id")
    fun deleteToDoItemByID(id: String)

    //Удалить все записи
    @Query("DELETE FROM to_do_items")
    fun cleanToDoItems()

    //Получить запись
    @Query("SELECT * FROM to_do_items WHERE to_do_item_id = :id")
    fun getToDoItemByID(id: String): ToDoItemDatabase?

    //Получить весь список
    @Query("SELECT * FROM to_do_items ORDER BY date_create")
    fun getAllItems(): LiveData<List<ToDoItemDatabase>?>
}