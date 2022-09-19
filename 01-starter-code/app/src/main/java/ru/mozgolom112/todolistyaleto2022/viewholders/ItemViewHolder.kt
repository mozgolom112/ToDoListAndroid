package ru.mozgolom112.todolistyaleto2022.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mozgolom112.todolistyaleto2022.database.ToDoItem
import ru.mozgolom112.todolistyaleto2022.databinding.ListItemBinding
import ru.mozgolom112.todolistyaleto2022.adapters.ToDoItemAdapter

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

    fun bind(
        item: ToDoItem,
        infoClickListener: ToDoItemAdapter.InfoClickListener,
        checkBoxStateClickListener: ToDoItemAdapter.CheckBoxStateClickListener
    ) {
        fulfillBinding(item)
        setClickListeners(item, infoClickListener, checkBoxStateClickListener)
    }


    private fun fulfillBinding(item: ToDoItem) {
        binding.apply {
            toDoItem = item
            checkBoxState.isChecked = item.isCompleted
            executePendingBindings()
        }
    }

    private fun setClickListeners(
        item: ToDoItem,
        infoClickListener: ToDoItemAdapter.InfoClickListener,
        checkBoxStateClickListener: ToDoItemAdapter.CheckBoxStateClickListener
    ) {
        binding.apply {
            btnInfo.setOnClickListener {
                infoClickListener.onClick(item)
            }
            checkBoxState.setOnClickListener {
                checkBoxStateClickListener.onClick(item)
            }
        }
    }
}