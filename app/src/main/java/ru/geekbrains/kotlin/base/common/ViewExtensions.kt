package ru.geekbrains.kotlin.base.common

import android.view.View

fun View.dip(value: Int): Int = context.dip(value)
fun View.dip(value: Float): Int = context.dip(value)