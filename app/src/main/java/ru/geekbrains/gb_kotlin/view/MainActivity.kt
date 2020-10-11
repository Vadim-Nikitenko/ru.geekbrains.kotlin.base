package ru.geekbrains.gb_kotlin.view

import android.os.Bundle
import android.widget.Adapter
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.geekbrains.gb_kotlin.viewmodel.MainViewModel
import ru.geekbrains.gb_kotlin.R
import ru.geekbrains.gb_kotlin.model.entity.Note

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    var adapter: NotesRVAdapter = NotesRVAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        rv_notes.layoutManager = GridLayoutManager(this, 2)
        rv_notes.adapter = adapter

        viewModel.viewState.observe(this, Observer { value ->
            value?.let {adapter.notes = it.notes }
        })

    }
}