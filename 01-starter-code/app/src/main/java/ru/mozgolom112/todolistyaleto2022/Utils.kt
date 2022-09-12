package ru.mozgolom112.todolistyaleto2022

import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import ru.mozgolom112.todolistyaleto2022.database.ToDoItem
import ru.mozgolom112.todolistyaleto2022.todoitemstracker.ToDoItemAdapter
import java.util.*

val MONTH = listOf(
    "января",
    "февраля",
    "марта",
    "апреля",
    "мая",
    "июня",
    "июля",
    "августа",
    "сентября",
    "октября",
    "ноября",
    "декабря"
)

enum class Priority(value: Int, text: String){
    HIGH(2, "!!Высоский"),
    NONE(0,"Нет"),
    LOW(1,"Низкий")
}

//Выпадает ошибка, если использовать его
@BindingAdapter("listData")
fun bindData(recyclerView: RecyclerView, data: List<ToDoItem?>){
    val adapter = recyclerView.adapter as ToDoItemAdapter
    adapter.submitList(data)
}

@BindingAdapter("dealineDate")
fun bindDate(textView: TextView, _date: Long) {
    if (_date > 0) {
        val c = Calendar.getInstance()
        c.timeInMillis = _date
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        textView.apply {
            text = "$day ${MONTH[month]} $year"
            visibility = View.VISIBLE
        }
    } else {
        textView.apply {
            text = "Нет даты"
            visibility = View.INVISIBLE
        }
    }
}

@BindingAdapter("isChecked")
fun isChecked(checkBox: CheckBox, _state: Boolean){
    checkBox.isChecked = _state
}

@BindingAdapter("isSwitchChecked")
fun isChecked(switchMaterial: SwitchMaterial, date: Long){
    switchMaterial.isChecked = date > 0
}

@BindingAdapter("setShortText")
fun setShortText(textView: TextView, text: String){
    //Не надо вставлять очень большой текст, даже не смотря на ограничение в три строчки
    val maxLength = 500 //TODO(сделать адаптивно считать значение, в зависимости от экрана)
    if (text.length > maxLength){
        Log.i("BindingAdapter","${text.dropLast(maxLength)}")
        textView.text = text.dropLast(maxLength)
    } else{
        textView.text = text
    }
}

@BindingAdapter("showPriority")
fun show(textView: TextView, priority: Int) = when(priority) {
    2 -> {
        textView.apply {
            visibility = View.VISIBLE
            text == "!!"
        }
    }
    else -> textView.visibility = View.GONE
}