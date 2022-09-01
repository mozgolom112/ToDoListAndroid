package ru.mozgolom112.todolistyaleto2022.todoitemstracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.mozgolom112.todolistyaleto2022.R
import ru.mozgolom112.todolistyaleto2022.database.ToDoItem
import ru.mozgolom112.todolistyaleto2022.databinding.HeaderBinding
import ru.mozgolom112.todolistyaleto2022.databinding.ListItemToDoItemBinding

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class ToDoItemAdapter() : ListAdapter<DataItem, RecyclerView.ViewHolder>(ToDoItemDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        getViewHolder(parent, viewType)

    private fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ToDoItemViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.ToDoDataItem -> ITEM_VIEW_TYPE_ITEM
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ToDoItemViewHolder -> {
                val item = getItem(position) as DataItem.ToDoDataItem
                holder.bind(item.toDoItem)
            }
            //is OtherViewHolder -> {...}
        }
    }

    fun addHeaderAndSubmitList(list: List<ToDoItem>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf<DataItem>(DataItem.Header)
                else -> listOf<DataItem>(DataItem.Header) + list.map { DataItem.ToDoDataItem(it) }
            }
            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }

    class ToDoItemViewHolder private constructor(val binding: ListItemToDoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ToDoItemViewHolder = ToDoItemViewHolder(getBinding(parent))

            private fun getBinding(parent: ViewGroup): ListItemToDoItemBinding {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemToDoItemBinding.inflate(layoutInflater, parent, false)
                return binding
            }
        }

        fun bind(item: ToDoItem) = fulfillBinding(item)

        private fun fulfillBinding(item: ToDoItem) {
            binding.apply {
                txtTestId.text = item.id
                executePendingBindings()
            }
        }
    }

    class HeaderViewHolder private constructor(binding: HeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder = HeaderViewHolder(getBinding(parent))

            private fun getBinding(parent: ViewGroup): HeaderBinding {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HeaderBinding.inflate(layoutInflater, parent, false)
                return binding
            }
        }
    }

    class ToDoItemDiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem) =
            oldItem == newItem
    }

}

sealed class DataItem() {
    data class ToDoDataItem(val toDoItem: ToDoItem) : DataItem() {
        override val id = toDoItem.id
    }

    object Header : DataItem() {
        override val id = EMPTY_ID
    }

    abstract val id: String
}