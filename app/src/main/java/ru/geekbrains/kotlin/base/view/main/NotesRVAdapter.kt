package ru.geekbrains.kotlin.base.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_note.view.*
import ru.geekbrains.kotlin.R
import ru.geekbrains.kotlin.base.model.entity.Note

class NotesRVAdapter(val onClickListener: ((Note) -> Unit)? = null): RecyclerView.Adapter<NotesRVAdapter.ViewHolder>() {
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

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(note: Note) = with(itemView) {
            tv_text.text = note.text
            tv_title.text = note.title

            val color = when(note.color){
                Note.Color.WHITE -> R.color.white
                Note.Color.YELLOW -> R.color.yellow
                Note.Color.GREEN -> R.color.green
                Note.Color.BLUE -> R.color.blue
                Note.Color.RED -> R.color.red
                Note.Color.VIOLET -> R.color.violet
            }

            (itemView as CardView).setCardBackgroundColor(ResourcesCompat.getColor(resources, color, null))

            itemView.setOnClickListener{
                onClickListener?.invoke(note)
            }
        }
    }
}