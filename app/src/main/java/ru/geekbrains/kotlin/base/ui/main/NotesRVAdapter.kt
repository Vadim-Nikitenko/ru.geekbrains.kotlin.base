package ru.geekbrains.kotlin.base.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_note.view.*
import ru.geekbrains.kotlin.R
import ru.geekbrains.kotlin.base.data.entity.Note

class NotesRVAdapter(val onClickListener: ((Note) -> Unit)? = null) : RecyclerView.Adapter<NotesRVAdapter.ViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_note,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(notes[position])
    override fun getItemCount() = notes.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Note) = with(itemView) {
            with(note) {
                tv_title.text = title
                tv_text.text = text

                val color = when (note.color) {
                    Note.Color.WHITE -> R.color.white
                    Note.Color.YELLOW -> R.color.yellow
                    Note.Color.GREEN -> R.color.green
                    Note.Color.BLUE -> R.color.blue
                    Note.Color.RED -> R.color.red
                    Note.Color.VIOLET -> R.color.violet
                }
                (itemView as CardView).setCardBackgroundColor(ResourcesCompat.getColor(resources, color, null))

                itemView.setOnClickListener {
                    onClickListener?.invoke(note)
                }
            }
        }
    }

}