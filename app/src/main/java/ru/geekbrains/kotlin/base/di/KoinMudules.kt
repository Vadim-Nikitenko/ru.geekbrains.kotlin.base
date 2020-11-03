package ru.geekbrains.kotlin.base.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import ru.geekbrains.kotlin.base.data.NotesRepository
import ru.geekbrains.kotlin.base.data.provider.DataProvider
import ru.geekbrains.kotlin.base.data.provider.FirestoreDataProvider
import ru.geekbrains.kotlin.base.ui.main.MainViewModel
import ru.geekbrains.kotlin.base.ui.note.NoteViewModel
import ru.geekbrains.kotlin.base.ui.splash.SplashViewModel


val appModule = module {
    //single кэширует инстанс и возвращает его один раз в отличие от factory
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    // get(), get() найдет FirebaseFirestore.getInstance() и FirebaseAuth.getInstance()
    // ошибка обнаружится в Runtime
    single { FirestoreDataProvider(get(), get()) } bind DataProvider::class
    single { NotesRepository(get()) }
}

val splashModule = module {
    // учитывает механизмы viewModel
    viewModel { SplashViewModel(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}