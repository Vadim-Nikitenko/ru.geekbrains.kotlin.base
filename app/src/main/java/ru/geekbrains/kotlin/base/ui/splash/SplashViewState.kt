package ru.geekbrains.kotlin.base.ui.splash

import ru.geekbrains.kotlin.base.ui.base.BaseViewState

class SplashViewState(authenticated: Boolean? = null, error: Throwable? = null) :
    BaseViewState<Boolean?>(authenticated, error)