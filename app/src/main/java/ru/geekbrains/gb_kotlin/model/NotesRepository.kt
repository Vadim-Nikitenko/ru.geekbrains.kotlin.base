package ru.geekbrains.gb_kotlin.model

import ru.geekbrains.gb_kotlin.model.entity.Note

//синглтон
object NotesRepository {
    val list = mutableListOf(
        Note(
            "Заметка #1",
            "Текст заметки #1",
            0x83253191.toInt()
        ),
        Note(
            "Заметка #2",
            "Текст заметки #2",
            0x24725383.toInt()
        ),
        Note(
            "Заметка #3",
            "Текст заметки #3",
            0x255159243.toInt()
        )
    )
}