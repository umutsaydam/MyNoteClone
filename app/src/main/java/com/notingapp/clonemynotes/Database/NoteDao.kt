package com.notingapp.clonemynotes.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.notingapp.clonemynotes.Models.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note)

    @Query("SELECT * FROM notes_table ORDER BY date")
    fun getAllNotes(): LiveData<List<Note>>

    @Query(
        "UPDATE notes_table SET title = :note_title," +
                " note = :note_desc, date = :note_date, bg_color = :bg_color" +
                " WHERE id = :note_id"
    )
    fun updateNote(
        note_id: Int?,
        note_title: String?,
        note_desc: String?,
        note_date: String?,
        bg_color: Int?
    )

    @Delete
    fun deleteNote(note : Note)

}