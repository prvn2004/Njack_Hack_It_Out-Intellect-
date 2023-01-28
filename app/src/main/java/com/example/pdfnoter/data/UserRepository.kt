package com.example.pdfnoter.data

import android.content.Context

class UserRepository(context: Context) {
    private val userDao = AppDatabase.getDatabase(context).userDao()
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


    fun insertNotes(notes: Notes) {
        notesDao.insert(notes)
    }

    fun deletenotesbyid(id: Long){
        notesDao.deleteByUserId(id)
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
