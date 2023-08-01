package com.notingapp.clonemynotes.Database

import androidx.lifecycle.LiveData
import com.notingapp.clonemynotes.Models.Note

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes :LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    suspend fun updateNote(note: Note){
        noteDao.updateNote(note.id, note.title, note.note, note.date, note.bg_color)
    }

    suspend fun deleteNote(note: Note){
        noteDao.deleteNote(note)
    }
}