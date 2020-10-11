package ru.geekbrains.gb_kotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.gb_kotlin.model.NotesRepository

class MainViewModel : ViewModel() {

    private val viewStateLiveData = MutableLiveData<MainViewState>()
//    val notes = MutableLiveData<List<Note>>()

    init {
        viewStateLiveData.value = MainViewState(NotesRepository.list)
    }

    val viewState
        get() = viewStateLiveData as LiveData<MainViewState>

//    fun viewState(): LiveData<MainViewState> = viewStateLiveData

}