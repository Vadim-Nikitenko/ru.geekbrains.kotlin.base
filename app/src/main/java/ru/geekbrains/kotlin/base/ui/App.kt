package ru.geekbrains.kotlin.base.ui

import android.app.Application
import org.koin.android.ext.android.startKoin
import ru.geekbrains.kotlin.base.di.appModule
import ru.geekbrains.kotlin.base.di.mainModule
import ru.geekbrains.kotlin.base.di.noteModule
import ru.geekbrains.kotlin.base.di.splashModule

class App : Application() {
    companion object {
        var instance: App? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin(this, listOf(appModule, splashModule, mainModule, noteModule))
    }
}