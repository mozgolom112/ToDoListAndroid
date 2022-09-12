package ru.mozgolom112.todolistyaleto2022.todoitemstracker

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.mozgolom112.todolistyaleto2022.database.ToDoItem
import ru.mozgolom112.todolistyaleto2022.todoitemstracker.diffUtils.DiffCallback
import ru.mozgolom112.todolistyaleto2022.todoitemstracker.viewHolders.ItemViewHolder

class ToDoItemAdapter(private val clickListener: OnItemClickListener) :
    ListAdapter<ToDoItem, ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder.from(parent)

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.apply {
            bind(item, clickListener)
//            На весь layout item'a
//            itemView.setOnClickListener(){
//                clickListener.onClick(item)
//            }
        }

    }

    class OnItemClickListener(val clickListener: (ToDoItem) -> Unit) {
        fun onClick(item: ToDoItem) = clickListener(item)
    }
}
