package ru.mozgolom112.todolistyaleto2022.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.mozgolom112.todolistyaleto2022.adapters.DiffCallback.ToDoItemDiffCallback
import ru.mozgolom112.todolistyaleto2022.domain.ToDoItem
import ru.mozgolom112.todolistyaleto2022.viewholders.ItemViewHolder

class ToDoItemAdapter(
    private val infoClickListener: InfoClickListener,
    private val checkBoxStateClickListener: CheckBoxStateClickListener
) :
    ListAdapter<ToDoItem, ItemViewHolder>(ToDoItemDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder.from(parent)

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position), infoClickListener, checkBoxStateClickListener)
    }

    //Навигация на фрагмент Detail
    class InfoClickListener(val clickListener: (ToDoItem) -> Unit) {
        fun onClick(item: ToDoItem) = clickListener(item)
    }

    //Изменение состояния дел
    class CheckBoxStateClickListener(val clickListener: (ToDoItem) -> Unit) {
        fun onClick(item: ToDoItem) = clickListener(item)
    }
}
