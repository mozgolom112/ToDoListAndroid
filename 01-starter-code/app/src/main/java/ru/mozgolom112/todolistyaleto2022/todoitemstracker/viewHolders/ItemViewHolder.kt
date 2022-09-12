package ru.mozgolom112.todolistyaleto2022.todoitemstracker.viewHolders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mozgolom112.todolistyaleto2022.database.ToDoItem
import ru.mozgolom112.todolistyaleto2022.databinding.ListItemBinding
import ru.mozgolom112.todolistyaleto2022.todoitemstracker.ToDoItemAdapter

class ItemViewHolder private constructor(private val binding: ListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): ItemViewHolder = ItemViewHolder(getBinding(parent))

        private fun getBinding(parent: ViewGroup): ListItemBinding {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemBinding.inflate(layoutInflater, parent, false)
            return binding
        }
    }

    fun bind(item: ToDoItem, clickListener: ToDoItemAdapter.OnItemClickListener) = fulfillBinding(item, clickListener)

    private fun fulfillBinding(item: ToDoItem, clickListener: ToDoItemAdapter.OnItemClickListener) {
        binding.apply {
            toDoItem = item
            btnInfo.setOnClickListener(){
                clickListener.onClick(item)
            }
            executePendingBindings()
        }
    }
}