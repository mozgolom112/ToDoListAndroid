package ru.mozgolom112.todolistyaleto2022.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@kotlinx.parcelize.Parcelize
@Entity(tableName = "to_do_items")
data class ToDoItem(
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "priority")
    var priority: Int = 0,
    @ColumnInfo(name = "date_deadline")
    var dateDeadline: Long = -1,
    @ColumnInfo(name = "date_modified")
    var dateModified: Long = -1,
    @ColumnInfo(name = "is_completed")
    var isCompleted: Boolean = false,

    @PrimaryKey
    @ColumnInfo(name = "to_do_item_id")
    var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "date_create")
    var dateCreate: Long = System.currentTimeMillis(),
) : Parcelable

