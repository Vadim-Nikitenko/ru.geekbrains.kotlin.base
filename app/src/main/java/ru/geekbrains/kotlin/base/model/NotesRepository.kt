package ru.geekbrains.kotlin.base.model

import androidx.lifecycle.MutableLiveData
import ru.geekbrains.kotlin.base.model.entity.Note
import java.util.*

//синглтон
object NotesRepository {

    private val notesLiveData = MutableLiveData<MutableList<Note>>()

    val notes = mutableListOf(
        Note(
            UUID.randomUUID().toString(),
            "Заметка #1",
            "Текст заметки #1",
            Note.Color.BLUE
        ),
        Note(
            UUID.randomUUID().toString(),
            "Заметка #2",
            "Текст заметки #2",
            Note.Color.GREEN
        ),
        Note(
            UUID.randomUUID().toString(),
            "Заметка #3",
            "Текст заметки #3",
            Note.Color.RED
        )
    )

    init {
        notesLiveData.value = notes
    }

    fun saveNote(note: Note) {
        addOrReplace(note)
        notesLiveData.value = notes
    }

    private fun addOrReplace(note: Note) {
        for (i in 0 until notes.size) {
            if (notes[i] == note) {
                notes[i] = note
                return
            }
        }
        notes.add(note)
    }

    fun getNotes() = notesLiveData
}