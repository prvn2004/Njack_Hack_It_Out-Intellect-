package com.example.pdfnoter.uploadpdf

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.pdfnoter.data.AppDatabase
import com.example.pdfnoter.data.Notes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class upload_all_notes(var context: Context) {
    var firestore = FirebaseFirestore.getInstance()

    fun upload_local_notes(pdfid: String){
        Log.d("hi", "$pdfid")
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("updating note......")
        progressDialog.setCancelable(false)
        progressDialog.show()
        GlobalScope.launch {
            val appDatabase = AppDatabase.getDatabase(context)
            val list_notes = appDatabase.notesDao().getByPdfId(pdfid)

            val t = list_notes.size
            var i =0
            for(i in 0 until  t){
            val data = list_notes[i]
                val ts: Long = data.timestamp
                val notesdate: String = DateFormat.format("mm-dd-yyyy", ts).toString()
                val pdfData = hashMapOf(
                    "notestitle" to data.notesTitle,
                    "notestext" to data.notesText,
                    "timestamp" to data.timestamp,
                    "notespdfid" to data.pdfId,
                    "notesdate" to notesdate,
                    "notesimage" to "",
                    "notesdrawing" to ""
                )
                val currentuser = FirebaseAuth.getInstance().currentUser!!.uid.toString()
                firestore.collection("pdfs").document(currentuser).collection("Pdfs").document(pdfid)
                    .collection("Notes").add(pdfData)
                    .addOnSuccessListener {
                        appDatabase.notesDao().deleteByUserId(data.notesid)
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
                    }
            }
        }
        val handler = Handler()
        handler.postDelayed({
            progressDialog.dismiss()
        }, 2000)
        if (progressDialog.isShowing) progressDialog.cancel()
    }
}