package com.example.pdfnoter.data

import androidx.lifecycle.LiveData

class UserRepository(private val userDao:UserDao) {
    fun getUsers(): List<User> = userDao.getAll()
    suspend fun addUser(user:User){
        userDao.insert(user)
    }

}