package ru.geekbrains.kotlin.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val model: IModel = Model()

    private val dataLiveData = MutableLiveData<String>()

    fun buttonClicked() {
        val data = model.getData()
        dataLiveData.value = data
    }

    fun getDataLiveData(): LiveData<String> = dataLiveData

}