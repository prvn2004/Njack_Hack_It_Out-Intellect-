package com.example.pdfnoter.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey val userId: String,
//    val pdfId: String // foreign key to PDF table
)

@Entity(tableName = "pdf")
data class PDF(
    @PrimaryKey val pdfId: String,
    val pdfDate: String,
    val pdfName: String,
    val pdfUrl: String,
    val timestamp: String,
)

@Entity(tableName = "notes")
data class Notes(
    @PrimaryKey val notesId: String,
    val notesDate: String,
    val notesDrawing: String,
    val notesImage: String,
    val notesPdfId: String, // foreign key to PDF table
    val notesText: String,
    val notesTitle: String,
    val timestamp: String,
    val pdfId: String,
)