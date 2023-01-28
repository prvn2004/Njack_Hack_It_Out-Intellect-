package com.example.pdfnoter.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotesDao {

    @Query("DELETE FROM notes WHERE notesid = :id")
    fun deleteByUserId(id: Long)

    @Insert
    fun insert(notes: Notes)

    @Query("SELECT * FROM notes")
    fun getAll(): List<Notes>

    @Query("SELECT * FROM notes WHERE pdfId = :id")
    fun getByPdfId(id: String): List<Notes>

    @Query("SELECT * FROM notes WHERE notesId = :id")
    fun getByNotesId(id: String): Notes
}