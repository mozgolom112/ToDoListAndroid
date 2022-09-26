package ru.mozgolom112.todolistyaleto2022.viewholders

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mozgolom112.todolistyaleto2022.R
import ru.mozgolom112.todolistyaleto2022.adapters.ToDoItemAdapter
import ru.mozgolom112.todolistyaleto2022.databinding.ListItemBinding
import ru.mozgolom112.todolistyaleto2022.domain.ToDoItem

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
        Log.i("ViewHolder", "Bind")
        fulfillBinding(item)
        setClickListeners(item, infoClickListener, checkBoxStateClickListener)
    }


    private fun fulfillBinding(item: ToDoItem) {
        binding.apply {
            toDoItem = item
            //checkBoxState.isChecked = item.isCompleted
            //txtTaskShort.paint.isStrikeThruText = item.isCompleted
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