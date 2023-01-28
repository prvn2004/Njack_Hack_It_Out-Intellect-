package com.example.pdfnoter.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(application: Application, context: Context) : AndroidViewModel(application) {

    private val repository: UserRepository

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        val notesDao = AppDatabase.getDatabase(application).notesDao()
        repository = UserRepository(context)
    }

    fun insertUser(user: User) = viewModelScope.launch {
        repository.insertUser(user)
    }

    fun getUserById(id: String) = repository.getUserById(id)

    fun insertNotes(notes: Notes) = viewModelScope.launch {
        repository.insertNotes(notes)
    }
    fun getNotesByPdfId(id: String) = repository.getNotesByPdfId(id)

    fun getNotesByNotesId(id: String) = repository.getNotesById(id)
}
