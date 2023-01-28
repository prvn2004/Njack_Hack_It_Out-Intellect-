package com.example.pdfnoter.notes

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.view.View
import com.example.pdfnoter.data.AppDatabase
import com.example.pdfnoter.data.Notes
import com.example.pdfnoter.databinding.FragmentShowpdfBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class notes_upload_local(var binding: FragmentShowpdfBinding, var context: Context) {

    fun save_note_local(
        notesText: String,
        notesTitle: String,
        pdfId: String,
        timestamp: Long,
        notesDate: String,
        notesDrawing: String,
        notesImage: String,
    ) {
        if (notesText.isNullOrEmpty() && notesTitle.isNullOrEmpty()) {
            binding.eventBottomSheet.visibility = View.GONE
            binding.notestext.text.clear()
            binding.notestitle.text.clear()
            binding.insertedImage.setImageResource(0)
        } else {
            val progressDialog = ProgressDialog(context)
            progressDialog.setMessage("No internet, saving locally......")
            progressDialog.setCancelable(false)
            progressDialog.show()
            GlobalScope.launch {
                val appDatabase = AppDatabase.getDatabase(context)
                val Note = Notes(
                    timestamp,
                    notesDate = notesDate,
                    notesDrawing = notesDrawing,
                    notesImage = notesImage,
                    notesText = notesText,
                    notesTitle = notesTitle,
                    timestamp = timestamp,
                    pdfId = pdfId
                )
                appDatabase.notesDao().insert(Note)
            }
            binding.eventBottomSheet.visibility = View.GONE
            binding.notestext.text.clear()
            binding.notestitle.text.clear()
            binding.insertedImage.setImageResource(0)
            binding.insertedDraw.setImageResource(0)
            val handler = Handler()
            handler.postDelayed({
                progressDialog.dismiss()
            }, 2000)
        }
    }
}