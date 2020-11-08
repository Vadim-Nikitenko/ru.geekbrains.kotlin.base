package ru.geekbrains.kotlin.base.data

import ru.geekbrains.kotlin.base.data.entity.Note
import ru.geekbrains.kotlin.base.data.provider.DataProvider

class NotesRepository(val dataProvider: DataProvider) {
    fun getNotes() = dataProvider.subscribeToNotes()
    suspend fun saveNote(note: Note) = dataProvider.saveNote(note)
    suspend fun getNoteById(id: String) = dataProvider.getNoteById(id)
    suspend fun deleteNote(id: String) = dataProvider.deleteNote(id)
    suspend fun getCurrentUser() = dataProvider.getCurrentUser()
}