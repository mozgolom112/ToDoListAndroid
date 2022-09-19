package ru.mozgolom112.todolistyaleto2022.adapters.DiffCallback

import androidx.recyclerview.widget.DiffUtil
import ru.mozgolom112.todolistyaleto2022.database.ToDoItem

object ToDoItemDiffCallback: DiffUtil.ItemCallback<ToDoItem>() {
    override fun areItemsTheSame(oldItem: ToDoItem, newItem: ToDoItem) =
        oldItem === newItem

    override fun areContentsTheSame(oldItem: ToDoItem, newItem: ToDoItem) =
        oldItem.id == newItem.id
}