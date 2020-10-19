package ru.geekbrains.kotlin.base.data

import ru.geekbrains.kotlin.base.data.entity.Note
import ru.geekbrains.kotlin.base.data.provider.DataProvider
import ru.geekbrains.kotlin.base.data.provider.FirestoreDataProvider

object NotesRepository {
    val dataProvider: DataProvider = FirestoreDataProvider()

    fun getNotes() = dataProvider.getNotes()
    fun saveNote(note: Note) = dataProvider.saveNote(note)
    fun getNoteById(id: String) = dataProvider.getNoteById(id)
}