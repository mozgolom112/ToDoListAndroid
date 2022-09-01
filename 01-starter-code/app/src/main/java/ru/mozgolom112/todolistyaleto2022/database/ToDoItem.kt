package ru.mozgolom112.todolistyaleto2022.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "to_do_items")
data class ToDoItem(
    @PrimaryKey
    @ColumnInfo(name = "to_do_item_id")
    var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "priority")
    var priority: Int = 0,
    @ColumnInfo(name = "is_completed")
    var isCompleted: Boolean = false,
    @ColumnInfo(name = "date_create")
    var dateCreate: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "date_deadline")
    var dateDeadline: Long = -1,
    @ColumnInfo(name = "date_modified")
    var dateModified: Long = -1,
)

