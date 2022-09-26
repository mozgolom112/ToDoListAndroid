package ru.mozgolom112.todolistyaleto2022.util

import ru.mozgolom112.todolistyaleto2022.database.ToDoItemDatabase
import ru.mozgolom112.todolistyaleto2022.domain.ToDoItem

fun ToDoItemDatabase.asDomainModel(): ToDoItem = mapDatabaseToDomain(this)
fun List<ToDoItemDatabase>.asDomainModel(): List<ToDoItem> = map { mapDatabaseToDomain(it) }

fun ToDoItem.asDatabaseModel(): ToDoItemDatabase = mapDomainToDatabase(this)
fun List<ToDoItem>.asDatabaseModel(): List<ToDoItemDatabase> = map { mapDomainToDatabase(it) }

fun mapDatabaseToDomain(it: ToDoItemDatabase) = ToDoItem(
    description = it.description,
    priority = it.priority,
    dateDeadline = it.dateDeadline,
    dateModified = it.dateModified,
    isCompleted = it.isCompleted,
    id = it.id,
    dateCreate = it.dateCreate
)

fun mapDomainToDatabase(it: ToDoItem) = ToDoItemDatabase(
    description = it.description,
    priority = it.priority,
    dateDeadline = it.dateDeadline,
    dateModified = it.dateModified,
    isCompleted = it.isCompleted,
    id = it.id,
    dateCreate = it.dateCreate
)