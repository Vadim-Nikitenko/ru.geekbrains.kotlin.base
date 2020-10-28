package ru.geekbrains.kotlin.base.ui.splash

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import ru.geekbrains.kotlin.base.ui.base.BaseActivity
import ru.geekbrains.kotlin.base.ui.main.MainActivity

class SplashActivity : BaseActivity<Boolean?, SplashViewState>() {

    companion object {
        fun start(context: Context) = Intent(context, SplashActivity::class.java).apply {
            context.startActivity(this)
        }
    }
    override val viewModel by lazy { ViewModelProvider(this).get(SplashViewModel::class.java) }
    override val layoutRes: Int? = null
    override fun renderData(data: Boolean?) {
        data?.takeIf { it }?.let {
            startMainActivity()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestUser()
    }

    private fun startMainActivity() {
        MainActivity.start(this)
        finish()
    }
}