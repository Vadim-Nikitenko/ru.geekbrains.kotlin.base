package ru.geekbrains.kotlin.base.ui.note

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.launch
import ru.geekbrains.kotlin.base.data.NotesRepository
import ru.geekbrains.kotlin.base.data.entity.Note
import ru.geekbrains.kotlin.base.ui.base.BaseViewModel

class NoteViewModel(val notesRepository: NotesRepository) : BaseViewModel<NoteData>() {

    private var pendingNote: Note? = null

    fun save(note: Note) {
        pendingNote = note
    }

    fun loadNote(id: String) = launch {
        try {
            notesRepository.getNoteById(id).let {
                pendingNote = it
                setData(NoteData(note = it))
            }
        } catch (e: Throwable) {
            setError(e)
        }
    }

    fun deleteNote() = launch {
        try {
            pendingNote?.let { notesRepository.deleteNote(it.id) }
            pendingNote = null
            setData(NoteData(isDeleted = true))
        } catch (e: Throwable) {
            setError(e)
        }
    }

    @VisibleForTesting
    public override fun onCleared() {
        launch {
            pendingNote?.let {
                notesRepository.saveNote(it)
            }
        }
    }
}