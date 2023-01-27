package com.example.pdfnoter.data

import android.content.Context

class UserRepository(context: Context) {
    private val userDao = AppDatabase.getDatabase(context).userDao()
    private val pdfDao = AppDatabase.getDatabase(context).pdfDao()
    private val notesDao = AppDatabase.getDatabase(context).notesDao()

    fun insertUser(user: User) {
        userDao.insert(user)
    }

    fun getAllUsers(): List<User> {
        return userDao.getAll()
    }

    fun getUserById(id: String): User {
        return userDao.getById(id)
    }

    fun insertPDF(pdf: PDF) {
        pdfDao.insert(pdf)
    }

    fun getAllPDFs(): List<PDF> {
        return pdfDao.getAll()
    }

    fun getPDFById(id: String): PDF {
        return pdfDao.getById(id)
    }

    fun insertNotes(notes: Notes) {
        notesDao.insert(notes)
    }

    fun getAllNotes(): List<Notes> {
        return notesDao.getAll()
    }

    fun getNotesByPdfId(id: String): List<Notes> {
        return notesDao.getByPdfId(id)
    }

    fun getNotesById(id: String): Notes {
        return notesDao.getByNotesId(id)
    }
}
