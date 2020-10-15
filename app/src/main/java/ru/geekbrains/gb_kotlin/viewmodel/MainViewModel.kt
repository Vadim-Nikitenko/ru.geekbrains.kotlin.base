package ru.geekbrains.gb_kotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.gb_kotlin.model.NotesRepository
import ru.geekbrains.gb_kotlin.model.entity.Note

class MainViewModel : ViewModel() {
    val notes = MutableLiveData<MutableList<Note>>()

    init {
        notes.value = NotesRepository.list
    }

//    val viewState
//        get() = viewStateLiveData as LiveData<MainViewState>

//    fun viewState(): LiveData<MainViewState> = viewStateLiveData

}