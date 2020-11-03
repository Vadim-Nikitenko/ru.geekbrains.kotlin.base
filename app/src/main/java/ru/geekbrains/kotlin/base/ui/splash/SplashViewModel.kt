package ru.geekbrains.kotlin.base.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import ru.geekbrains.kotlin.base.data.NotesRepository
import ru.geekbrains.kotlin.base.data.entity.User
import ru.geekbrains.kotlin.base.data.errors.NoAuthException
import ru.geekbrains.kotlin.base.ui.base.BaseViewModel

class SplashViewModel(val notesRepository: NotesRepository) : BaseViewModel<Boolean?, SplashViewState>() {


    private var userLiveData: LiveData<User?>? = null

    private val userObserver = object : Observer<User?> {
        override fun onChanged(user: User?) {
            viewStateLiveData.value =
                user?.let { SplashViewState((true)) } ?: SplashViewState(error = NoAuthException())
            userLiveData?.removeObserver(this)
        }
    }

    fun requestUser() {
        userLiveData = notesRepository.getCurrentUser()
        userLiveData?.observeForever(userObserver)
    }

    override fun onCleared() {
        super.onCleared()
        userLiveData?.removeObserver(userObserver)
    }
}