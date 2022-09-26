package ru.mozgolom112.todolistyaleto2022.domain

import android.os.Parcelable
import java.util.*

@kotlinx.parcelize.Parcelize
data class ToDoItem(
    var description: String,
    var priority: Int = 0,
    var dateDeadline: Long = -1,
    var dateModified: Long = -1,
    var isCompleted: Boolean = false,
    var id: String = UUID.randomUUID().toString(),
    var dateCreate: Long = System.currentTimeMillis(),
) : Parcelable
