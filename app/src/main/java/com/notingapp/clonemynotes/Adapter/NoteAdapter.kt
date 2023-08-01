package com.notingapp.clonemynotes.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.notingapp.clonemynotes.Models.Note
import com.notingapp.clonemynotes.R

class NoteAdapter(private val context: Context, val listener: NotesClickListener) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    private val notesList = arrayListOf<Note>()
    private val fullList = arrayListOf<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currNote = notesList[position]
        holder.tv_title.text = currNote.title
        holder.tv_note.text = currNote.note
        holder.tv_date.text = currNote.date

        holder.noteCardView.setBackgroundColor(currNote.bg_color)

        holder.noteCardView.setOnClickListener {
            listener.onItemClickListener(notesList[position])
        }

        holder.noteCardView.setOnLongClickListener {
            listener.onLongItemClickListener(notesList[position], holder.noteCardView)
            true
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun filterNotes(filteredNote: String) {
        notesList.clear()
        fullList.forEach {
            if (it.title.lowercase().contains(filteredNote) || it.note.lowercase().contains(filteredNote)) {
                notesList.add(it)
            }
        }
        notifyDataSetChanged()
    }

    fun updateList(list: List<Note>) {
        notesList.clear()
        notesList.addAll(list)

        fullList.clear()
        fullList.addAll(list)
        notifyDataSetChanged()
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteCardView = itemView.findViewById<CardView>(R.id.noteCardView)
        val tv_title = itemView.findViewById<TextView>(R.id.tv_title)
        val tv_note = itemView.findViewById<TextView>(R.id.tv_note)
        val tv_date = itemView.findViewById<TextView>(R.id.tv_date)
    }

    interface NotesClickListener {
        fun onItemClickListener(note: Note)
        fun onLongItemClickListener(note: Note, cardView: CardView)
    }
}