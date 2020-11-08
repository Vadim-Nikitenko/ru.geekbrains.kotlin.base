package ru.geekbrains.kotlin.base.ui.splash

import kotlinx.coroutines.launch
import ru.geekbrains.kotlin.base.data.NotesRepository
import ru.geekbrains.kotlin.base.data.errors.NoAuthException
import ru.geekbrains.kotlin.base.ui.base.BaseViewModel

class SplashViewModel(val notesRepository: NotesRepository) : BaseViewModel<Boolean>() {

    fun requestUser() = launch {
        notesRepository.getCurrentUser()?.let {
            setData(true)
        } ?: setError(NoAuthException())
    }
}
