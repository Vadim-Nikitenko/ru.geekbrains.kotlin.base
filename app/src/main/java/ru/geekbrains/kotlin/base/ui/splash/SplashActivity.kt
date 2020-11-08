package ru.geekbrains.kotlin.base.ui.splash

import android.content.Context
import android.content.Intent
import org.koin.android.viewmodel.ext.android.viewModel
import ru.geekbrains.kotlin.base.ui.base.BaseActivity
import ru.geekbrains.kotlin.base.ui.main.MainActivity
import java.util.*

class SplashActivity : BaseActivity<Boolean>() {

    companion object {
        fun start(context: Context) = Intent(context, SplashActivity::class.java).apply {
            context.startActivity(this)
        }
    }

    override val viewModel: SplashViewModel by viewModel()
    override val layoutRes = null

    override fun onResume() {
        super.onResume()
        viewModel.requestUser()
    }

    override fun renderData(data: Boolean) {
        data?.takeIf { it }?.let {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        MainActivity.start(this)
        finish()
    }
}