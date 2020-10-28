package ru.geekbrains.kotlin.base.ui.note

import ru.geekbrains.kotlin.base.data.entity.Note
import ru.geekbrains.kotlin.base.ui.base.BaseViewState

class NoteViewState(data: Data = Data(), error: Throwable? = null) : BaseViewState<NoteViewState.Data>(data, error) {
    class Data(val note: Note? = null, val isDeleted: Boolean = false)
}