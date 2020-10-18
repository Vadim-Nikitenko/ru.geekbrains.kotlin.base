package ru.geekbrains.kotlin.base.viewmodel.note

import androidx.lifecycle.ViewModel
import ru.geekbrains.kotlin.base.model.NotesRepository
import ru.geekbrains.kotlin.base.model.entity.Note

class NoteViewModel(): ViewModel() {
    private var pendingNote: Note? = null

    fun save(note: Note?) {
        pendingNote = note
    }

    // при уничтожении вьюмодел
    override fun onCleared() {
        pendingNote?.let {
            NotesRepository.saveNote(it)
        }
    }

}