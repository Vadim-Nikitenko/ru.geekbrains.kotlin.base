package ru.geekbrains.kotlin.base.data

import ru.geekbrains.kotlin.base.data.entity.Note
import ru.geekbrains.kotlin.base.data.provider.DataProvider

class NotesRepository(val dataProvider: DataProvider) {
    fun getNotes() = dataProvider.getNotes()
    fun saveNote(note: Note) = dataProvider.saveNote(note)
    fun getNoteById(id: String) = dataProvider.getNoteById(id)
    fun getCurrentUser() = dataProvider.getCurrentUser()
    fun deleteNote(id: String) = dataProvider.deleteNote(id)
}