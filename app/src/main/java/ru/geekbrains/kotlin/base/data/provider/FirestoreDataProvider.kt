package ru.geekbrains.kotlin.base.data.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ru.geekbrains.kotlin.base.data.entity.Note
import ru.geekbrains.kotlin.base.data.entity.User
import ru.geekbrains.kotlin.base.data.errors.NoAuthException
import ru.geekbrains.kotlin.base.data.model.NoteResult

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

    override fun getCurrentUser(): LiveData<User?> = MutableLiveData<User?>().apply {
        value = currentUser?.let { User(it.displayName ?: "", it.email ?: "") }
    }

    override fun getNotes(): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        notesReference.addSnapshotListener { snapshot, error ->
            error?.let {
                value = NoteResult.Error(it)
                return@addSnapshotListener
            }
            snapshot?.let {
                val notes = it.documents.map { it.toObject(Note::class.java) }
                value = NoteResult.Success(notes)
            }
        }
    }

    override fun saveNote(note: Note) = MutableLiveData<NoteResult>().apply {
        notesReference.document(note.id).set(note)
            .addOnSuccessListener {
                value = NoteResult.Success(note)
            }.addOnFailureListener {
                value = NoteResult.Error(it)
            }
    }

    override fun getNoteById(id: String) = MutableLiveData<NoteResult>().apply {
        notesReference.document(id).get()
            .addOnSuccessListener { snapshot ->
                val note = snapshot.toObject(Note::class.java) as Note
                value = NoteResult.Success(note)
            }.addOnFailureListener {
                value = NoteResult.Error(it)
            }
    }

    override fun deleteNote(id: String) = MutableLiveData<NoteResult>().apply {
        notesReference.document(id).delete()
            .addOnSuccessListener {
                value = NoteResult.Success(null)
            }.addOnFailureListener {
                value = NoteResult.Error(it)
            }
    }



}