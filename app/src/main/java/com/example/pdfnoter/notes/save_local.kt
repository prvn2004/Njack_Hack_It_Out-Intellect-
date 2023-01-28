package com.example.pdfnoter.notes

import android.app.ProgressDialog
import android.content.Context
import androidx.room.PrimaryKey
import com.example.pdfnoter.data.AppDatabase
import com.example.pdfnoter.data.Notes
import com.example.pdfnoter.data.User
import com.example.pdfnoter.databinding.FragmentShowpdfBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class save_local(var context: Context) {

    fun save_user(userId: String){
        GlobalScope.launch {
            val appDatabase = AppDatabase.getDatabase(context)
            val user = User(userId = userId)
            appDatabase.userDao().insert(user)
        }
    }
}