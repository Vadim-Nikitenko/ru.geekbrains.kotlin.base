package ru.geekbrains.kotlin.base.common

import android.content.Context
import androidx.core.content.res.ResourcesCompat
import ru.geekbrains.kotlin.R
import ru.geekbrains.kotlin.base.data.entity.Note

//функции расширения
fun Note.Color.getColorRes() = when (this) {
    Note.Color.WHITE -> R.color.white
    Note.Color.YELLOW -> R.color.yellow
    Note.Color.GREEN -> R.color.green
    Note.Color.BLUE -> R.color.blue
    Note.Color.RED -> R.color.red
    Note.Color.VIOLET -> R.color.violet
}

fun Note.Color.getColorInt(context: Context) =
    ResourcesCompat.getColor(context.resources, getColorRes(), null)