package ru.geekbrains.kotlin.base.ui.note

import ru.geekbrains.kotlin.base.data.entity.Note

data class NoteData(val note: Note? = null, val isDeleted: Boolean = false)