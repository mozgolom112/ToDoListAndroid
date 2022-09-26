package ru.mozgolom112.todolistyaleto2022.util


const val EMPTY_STRING = ""

enum class DetailErrors(val text: String) {
    EMPTY_ERROR(""),
    EMPTY_DESCRIPTION_ERROR("Пожалуйста, добавьте текст задачи. Он не может быть пустым"),
    INSERT_UNIQUE_ITEM_ERROR("Упс! Дело с таким ID уже существует. Явно произошла какая-то ошибка. Вы все равно хотите создать дело? Да | Нет"),
    UPDATE_NON_EXISTENT_ITEM("В базе данных дела с таким ID больше не существует. Возможно он уже был удален. Хотите создать новое дело? Да | Нет")
}
