package ru.geekbrains.kotlin.base.ui.note

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.geekbrains.kotlin.base.data.NotesRepository
import ru.geekbrains.kotlin.base.data.entity.Note
import ru.geekbrains.kotlin.base.data.model.NoteResult

class NoteViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<NotesRepository>()
    private val noteLiveData = MutableLiveData<NoteResult>()

    private val testNote = Note("1", "title1", "text1")

    private lateinit var viewModel: NoteViewModel

    @Before
    fun setup() {
        clearAllMocks()
        every { mockRepository.getNoteById(testNote.id) } returns noteLiveData
        every { mockRepository.deleteNote(testNote.id) } returns noteLiveData
        every { mockRepository.saveNote(any()) } returns noteLiveData
        viewModel = NoteViewModel(mockRepository)
    }


    @Test
    fun `loadNotee should return NoteViewState Data`() {
        var result: NoteViewState.Data? = null
        val testData = NoteViewState.Data(testNote, false)
        viewModel.viewStateLiveData.observeForever {
            result = it.data
        }
        viewModel.loadNote(testNote.id)
        noteLiveData.value = NoteResult.Success(testNote)
        assertEquals(testData, result)
    }


    @Test
    fun `loadNote should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")
        viewModel.viewStateLiveData.observeForever {
            result = it.error
        }
        viewModel.loadNote(testNote.id)
        noteLiveData.value = NoteResult.Error(testData)
        assertEquals(testData, result)
    }

    @Test
    fun `deleteNote should return NoteViewState with isDeleted`() {
        var result: NoteViewState.Data? = null
        viewModel.viewStateLiveData.observeForever {
            result = it.data
        }
        viewModel.loadNote(testNote.id)
        noteLiveData.value = NoteResult.Success(testNote)

        viewModel.deleteNote()
        noteLiveData.value = NoteResult.Success(null)
        assertEquals(result?.isDeleted, true)
    }

    @Test
    fun `deleteNote returns error`() {
        var result: Throwable? = null
        val testData = Throwable("error")
        viewModel.viewStateLiveData.observeForever {
            result = it.error
        }
        viewModel.save(testNote)

        viewModel.deleteNote()
        noteLiveData.value = NoteResult.Error(testData)

        assertEquals(testData, result)
    }


    @Test
    fun `should save changes`() {
        viewModel.loadNote(testNote.id)
        noteLiveData.value = NoteResult.Success(testNote)

        viewModel.onCleared()
        verify(exactly = 1) { mockRepository.saveNote(testNote) }
    }

    @Test
    fun `should delete changes`() {
        viewModel.loadNote(testNote.id)
        noteLiveData.value = NoteResult.Success(testNote)
        viewModel.deleteNote()

        viewModel.onCleared()
        verify(exactly = 1) { mockRepository.deleteNote(testNote.id) }
    }

}