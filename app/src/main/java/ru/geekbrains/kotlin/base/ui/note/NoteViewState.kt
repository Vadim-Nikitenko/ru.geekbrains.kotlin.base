package ru.geekbrains.kotlin.base.ui.note

import ru.geekbrains.kotlin.base.data.entity.Note
import ru.geekbrains.kotlin.base.ui.base.BaseViewState

class NoteViewState(note: Note? = null, error: Throwable? = null) : BaseViewState<Note?>(note, error)