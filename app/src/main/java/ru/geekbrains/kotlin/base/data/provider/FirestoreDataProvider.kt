package ru.geekbrains.kotlin.base.data.provider

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import ru.geekbrains.kotlin.base.data.entity.Note
import ru.geekbrains.kotlin.base.data.entity.User
import ru.geekbrains.kotlin.base.data.errors.NoAuthException
import ru.geekbrains.kotlin.base.data.model.NoteResult
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirestoreDataProvider(val store: FirebaseFirestore, val auth: FirebaseAuth) : DataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes"
        private const val USERS_COLLECTION = "users"
    }

    private val notesReference
        get() = currentUser?.let {
            store.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
        } ?: throw NoAuthException()

    private val currentUser
        get() = auth.currentUser

    override suspend fun getCurrentUser(): User? = suspendCoroutine { continuation ->
        val user = currentUser?.let { User(it.displayName ?: "", it.email ?: "") }
        continuation.resume(user)
    }

    override fun subscribeToNotes(): ReceiveChannel<NoteResult> =
        Channel<NoteResult>(Channel.CONFLATED).apply {
            try {
                notesReference.addSnapshotListener { snapshot, error ->
                    val value = error?.let {
                        NoteResult.Error(it)
                    } ?: snapshot?.let {
                        val notes = it.documents.map { it.toObject(Note::class.java) }
                        NoteResult.Success(notes)
                    }
                    value?.let { offer(it) }
                }
            } catch (e: Throwable) {
                offer(NoteResult.Error(e))
            }
        }

    override suspend fun saveNote(note: Note): Note = suspendCoroutine { continuation ->
        try {
            notesReference.document(note.id).set(note)
                .addOnSuccessListener {
                    continuation.resume(note)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        } catch (e: Throwable) {
            continuation.resumeWithException(e)
        }
    }

    override suspend fun deleteNote(id: String): Unit = suspendCoroutine { continuation ->
        try {
            notesReference.document(id).delete()
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }

        } catch (e: Throwable) {
            continuation.resumeWithException(e)
        }
    }

    override suspend fun getNoteById(id: String): Note = suspendCoroutine { continuation ->
        try {
            notesReference.document(id).get()
                .addOnSuccessListener { snapshot ->
                    val note = snapshot.toObject(Note::class.java) as Note
                    continuation.resume(note)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        } catch (e: Throwable) {
            continuation.resumeWithException(e)
        }
    }


}