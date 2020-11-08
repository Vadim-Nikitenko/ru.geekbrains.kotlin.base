package ru.geekbrains.kotlin.base.ui.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.geekbrains.kotlin.base.data.NotesRepository
import ru.geekbrains.kotlin.base.data.entity.User

class SplashViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()
    private val userLiveData = MutableLiveData<User?>()

    private val mockRepository = mockk<NotesRepository>()
    private lateinit var viewModel: SplashViewModel

    @Before
    fun setup() {
        clearAllMocks()
        every { mockRepository.getCurrentUser() } returns userLiveData
        viewModel = SplashViewModel(mockRepository)
    }

    @Test
    fun `userLiveData shouldn't have observers`() {
        viewModel.onCleared()
        Assert.assertFalse(userLiveData.hasObservers())
    }

    @Test
    fun `userLiveData should have observers`() {
        viewModel.requestUser()
        verify(exactly = 1) { mockRepository.getCurrentUser() }
        Assert.assertTrue(userLiveData.hasObservers())
    }

    @Test
    fun `should getCurrentUser() once`() {
        viewModel.requestUser()
        verify(exactly = 1) { mockRepository.getCurrentUser() }
    }
}