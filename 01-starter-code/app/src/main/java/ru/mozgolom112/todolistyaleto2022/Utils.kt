package ru.mozgolom112.todolistyaleto2022

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
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