package com.example.pdfnoter.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PdfDao {
    @Insert
    fun insert(pdf: PDF)

    @Query("SELECT * FROM pdf")
    fun getAll(): List<PDF>

    @Query("SELECT * FROM pdf WHERE pdfId = :id")
    fun getById(id: String): PDF
}