package ru.geekbrains.kotlin.base.data.provider

import kotlinx.coroutines.channels.ReceiveChannel
import ru.geekbrains.kotlin.base.data.entity.Note
import ru.geekbrains.kotlin.base.data.entity.User
import ru.geekbrains.kotlin.base.data.model.NoteResult

interface DataProvider {
    fun subscribeToNotes(): ReceiveChannel<NoteResult>
    suspend fun saveNote(note: Note): Note
    suspend fun getNoteById(id: String): Note
    suspend fun deleteNote(id: String)
    suspend fun getCurrentUser(): User?
}