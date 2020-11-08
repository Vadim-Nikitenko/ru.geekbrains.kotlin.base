package ru.geekbrains.kotlin.base.data.provider

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import io.mockk.*
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.geekbrains.kotlin.base.data.entity.Note
import ru.geekbrains.kotlin.base.data.errors.NoAuthException
import ru.geekbrains.kotlin.base.data.model.NoteResult

class FirestoreDataProviderTest {

    //правило, которое вызывает асинхронные таски сразу же. @get применить к геттеру в Java
    // например, notesReference.document(id).get() - Task, на который вешаем listeners
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    // мок для FirebaseFirestore
    private val mockDb = mockk<FirebaseFirestore>()
    private val mockAuth = mockk<FirebaseAuth>()
    private val mockUser = mockk<FirebaseUser>()
    private val mockResultCollection = mockk<CollectionReference>()

    private val mockDocument1 = mockk<DocumentSnapshot>()
    private val mockDocument2 = mockk<DocumentSnapshot>()
    private val mockDocument3 = mockk<DocumentSnapshot>()

    private val testNotes = listOf(Note("1"), Note("2"), Note("3"))

    private val provider = FirestoreDataProvider(mockDb, mockAuth)

    @Before
    fun setup() {
        clearAllMocks()
        every { mockAuth.currentUser } returns mockUser
        every { mockUser.uid } returns ""
        every {
            mockDb.collection(any()).document(any()).collection(any())
        } returns mockResultCollection

        // каст DocumentSnapshot к Note
        every { mockDocument1.toObject(Note::class.java) } returns testNotes[0]
        every { mockDocument2.toObject(Note::class.java) } returns testNotes[1]
        every { mockDocument3.toObject(Note::class.java) } returns testNotes[2]
    }

    @Test
    fun `getNotes should throw NoAuthException if no auth`() {
        var result: Any? = null
        every { mockAuth.currentUser } returns null
        provider.getNotes().observeForever {
            result = (it as? NoteResult.Error)?.error
        }
        assertTrue(result is NoAuthException)
    }


    @Test
    fun `getNotes should return notes`() {
        var result: List<Note>? = null
        val mockSnapshot = mockk<QuerySnapshot>()
        val slot = slot<EventListener<QuerySnapshot>>()
        every { mockSnapshot.documents } returns listOf(mockDocument1, mockDocument2, mockDocument3)
        every { mockResultCollection.addSnapshotListener(capture(slot)) } returns mockk()

        provider.getNotes().observeForever {
            result = (it as NoteResult.Success<List<Note>>)?.data
        }

        slot.captured.onEvent(mockSnapshot, null)

        assertEquals(testNotes, result)
    }

    @Test
    fun `getNotes should return error`() {
        var result: Throwable? = null
        val testError = mockk<FirebaseFirestoreException>()
        val slot = slot<EventListener<QuerySnapshot>>()
        every { mockResultCollection.addSnapshotListener(capture(slot)) } returns mockk()

        provider.getNotes().observeForever {
            result = (it as NoteResult.Error).error
        }
        slot.captured.onEvent(null, testError)
        assertEquals(testError, result)
    }

    @Test
    fun `saveNote should throw NoAuthException if no auth`() {
        var result: Any? = null
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every { mockAuth.currentUser } returns null
        provider.saveNote(testNotes[0]).observeForever {
            result = (it as? NoteResult.Error)?.error
        }
        assertTrue(result is NoAuthException)
    }

    @Test
    fun `saveNote calls set`() {
        // notesReference.document
        val mockDocumentReference = mockk<DocumentReference>()
        // когда просим первую заметку, вернут mockDocumentReference
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        provider.saveNote(testNotes[0])
        // проверяем, что был вызван set(testNotes[0]) один раз
        verify(exactly = 1) { mockDocumentReference.set(testNotes[0]) }
    }

    @Test
    fun `saveNote returns note`() {
        var result: Note? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<Void>>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every {
            mockDocumentReference.set(testNotes[0]).addOnSuccessListener(capture(slot))
                .addOnFailureListener(any())
        } returns mockk()

        provider.saveNote(testNotes[0]).observeForever {
            result = (it as NoteResult.Success<Note>)?.data
        }
        slot.captured.onSuccess(null)
        assertEquals(testNotes[0], result)
    }

    @Test
    fun `saveNote returns Throwable`() {
        var result: Throwable? = null
        val testError = mockk<FirebaseFirestoreException>()
        // notesReference.document
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnFailureListener>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every {
            mockDocumentReference.set(testNotes[0]).addOnSuccessListener(any())
                .addOnFailureListener(capture(slot))
        } returns mockk()
        provider.saveNote(testNotes[0]).observeForever {
            result = (it as NoteResult.Error)?.error
        }
        slot.captured.onFailure(testError)
        assertEquals(result, testError)
    }

    @Test
    fun `deleteNote calls delete`() {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        provider.deleteNote(testNotes[0].id)
        verify(exactly = 1) { mockDocumentReference.delete() }
    }

    @Test
    fun `deleteNote should throw NoAuthException if no auth`() {
        var result: Any? = null
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every { mockAuth.currentUser } returns null
        provider.deleteNote(testNotes[0].id).observeForever {
            result = (it as? NoteResult.Error)?.error
        }
        assertTrue(result is NoAuthException)
    }


    @Test
    fun `deleteNote returns success`() {
        var result: Note? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<Void>>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every {
            mockDocumentReference.delete().addOnSuccessListener(capture(slot))
                .addOnFailureListener(any())
        } returns mockk()
        provider.deleteNote(testNotes[0].id).observeForever {
            result = (it as NoteResult.Success<Note>)?.data
        }
        slot.captured.onSuccess(null)
        assertEquals(null, result)
    }

    @Test
    fun `deleteNote returns Throwable`() {
        var result: Throwable? = null
        val testError = mockk<FirebaseFirestoreException>()
        // notesReference.document
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnFailureListener>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every {
            mockDocumentReference.delete().addOnSuccessListener(any())
                .addOnFailureListener(capture(slot))
        } returns mockk()
        provider.deleteNote(testNotes[0].id).observeForever {
            result = (it as NoteResult.Error)?.error
        }
        slot.captured.onFailure(testError)
        assertEquals(result, testError)
    }


    @Test
    fun `getNoteById calls get`() {
        // notesReference.document
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        provider.getNoteById(testNotes[0].id)
        verify(exactly = 1) { mockDocumentReference.get() }
    }

    @Test
    fun `getNoteById should throw NoAuthException if no auth`() {
        var result: Any? = null
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every { mockAuth.currentUser } returns null
        provider.getNoteById(testNotes[0].id).observeForever {
            result = (it as? NoteResult.Error)?.error
        }
        assertTrue(result is NoAuthException)
    }

    @Test
    fun `getNoteById should return note`() {
        var result: Note? = null

        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<Void>>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every {
            mockDocumentReference.set(testNotes[0]).addOnSuccessListener(capture(slot))
                .addOnFailureListener(any())
        } returns mockk()

        provider.getNoteById(testNotes[0].id).observeForever {
            result = (it as NoteResult.Success<Note>)?.data
        }
        slot.captured.onSuccess(null)
        assertEquals(testNotes[0], result)
    }

    @Test
    fun `getNoteById should return Throwable`() {
        var result: Any? = null
        val testError = mockk<FirebaseFirestoreException>()

        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnFailureListener>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        every {
            mockDocumentReference.get().addOnSuccessListener(any())
                .addOnFailureListener(capture(slot))
        } returns mockk()
        provider.getNoteById(testNotes[0].id).observeForever {
            result = (it as NoteResult.Error)?.error
        }
        slot.captured.onFailure(testError)
        assertEquals(testError, result)
    }
}