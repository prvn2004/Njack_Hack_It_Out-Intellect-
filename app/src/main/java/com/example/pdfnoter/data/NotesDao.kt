package com.example.pdfnoter.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotesDao {
    @Insert
    fun insert(notes: Notes)

    @Query("SELECT * FROM notes")
    fun getAll(): List<Notes>

    @Query("SELECT * FROM notes WHERE notesPdfId = :id")
    fun getByPdfId(id: String): List<Notes>
}