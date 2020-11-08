package ru.geekbrains.kotlin.base.ui.main

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Observer
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import ru.geekbrains.kotlin.base.data.NotesRepository
import ru.geekbrains.kotlin.base.data.entity.Note
import ru.geekbrains.kotlin.base.data.model.NoteResult
import ru.geekbrains.kotlin.base.ui.base.BaseViewModel

class MainViewModel(val notesRepository: NotesRepository) : BaseViewModel<List<Note>?>() {

    private val repositoryNotes = notesRepository.getNotes()

    init {
        launch {
            repositoryNotes.consumeEach { result ->
                when (result) {
                    is NoteResult.Success<*> -> setData(result.data as? List<Note>)
                    is NoteResult.Error -> setError(result.error)
                }
            }
        }
    }

    @VisibleForTesting
    public override fun onCleared() {
        super.onCleared()
        repositoryNotes.cancel()
    }

}