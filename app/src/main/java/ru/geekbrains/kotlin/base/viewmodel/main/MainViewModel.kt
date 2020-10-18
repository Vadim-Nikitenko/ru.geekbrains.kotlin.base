package ru.geekbrains.kotlin.base.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.kotlin.base.model.NotesRepository
import ru.geekbrains.kotlin.base.model.entity.Note

class MainViewModel : ViewModel() {
    val notes = MutableLiveData<MutableList<Note>>()

    init {
        NotesRepository.getNotes().observeForever {
            notes.value = it
        }
    }

//    val viewState
//        get() = viewStateLiveData as LiveData<MainViewState>

//    fun viewState(): LiveData<MainViewState> = viewStateLiveData

}