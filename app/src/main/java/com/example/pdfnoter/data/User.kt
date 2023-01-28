package com.example.pdfnoter.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey val userId: String,
//    val pdfId: String // foreign key to PDF table
)


@Entity(tableName = "notes")

data class Notes(
    @PrimaryKey(autoGenerate = true) val notesid: Long,
    val notesDate: String,
    val notesDrawing: String,
    val notesImage: String,
    val notesText: String,
    val notesTitle: String,
    val timestamp: Long,
    val pdfId: String,
)

