package ru.mozgolom112.todolistyaleto2022.todoitemstracker.diffUtils

import androidx.recyclerview.widget.DiffUtil
import ru.mozgolom112.todolistyaleto2022.database.ToDoItem

object DiffCallback: DiffUtil.ItemCallback<ToDoItem>() {
    override fun areItemsTheSame(oldItem: ToDoItem, newItem: ToDoItem) =
        oldItem === newItem

    override fun areContentsTheSame(oldItem: ToDoItem, newItem: ToDoItem) =
        oldItem.id == newItem.id
}