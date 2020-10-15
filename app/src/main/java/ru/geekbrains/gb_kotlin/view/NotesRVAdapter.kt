package ru.geekbrains.gb_kotlin.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_note.view.*
import ru.geekbrains.gb_kotlin.R
import ru.geekbrains.gb_kotlin.model.entity.Note

class NotesRVAdapter: RecyclerView.Adapter<NotesRVAdapter.ViewHolder>() {
    var notes: MutableList<Note> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_note,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(notes[position])

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(note: Note) = with(itemView) {
            tv_text.text = note.text
            tv_title.text = note.title
            setBackgroundColor(note.color)
        }
    }
}