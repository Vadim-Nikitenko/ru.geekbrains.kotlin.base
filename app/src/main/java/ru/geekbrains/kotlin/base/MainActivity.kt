package ru.geekbrains.kotlin.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        button.setOnClickListener {
            viewModel.buttonClicked()
        }

        viewModel.getDataLiveData().observe(this, { value ->
            Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
        })

    }
}