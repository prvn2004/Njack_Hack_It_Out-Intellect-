package com.example.pdfnoter.notes

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.pdfnoter.databinding.FragmentShowpdfBinding
import com.example.pdfnoter.showpdf.showpdfFrag
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class upload_image(private var binding: FragmentShowpdfBinding, var context: Context, var checknote:Int, var selectedImageUri:Uri) {

    val storage = FirebaseStorage.getInstance()
    var firestore = FirebaseFirestore.getInstance()

    fun uploadimage(notestitle: String, notestext: String, notespdfid: String, docid1:String, pdfid1:String){
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("uploading image......")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val tsLong = System.currentTimeMillis()
        val ts: Long = tsLong
        val storageRef = storage.reference
        val imageRef = storageRef.child("images/${selectedImageUri?.lastPathSegment}")
        val uploadTask = imageRef.putFile(selectedImageUri!!)

        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                val downloadUrl = it.toString()
                if (checknote == 1){
                    edit_notes(binding, context).editdoc(notestitle, notestext,downloadUrl, docid1, pdfid1)
                }else{
                    upload_drawing(binding, context, selectedImageUri, checknote).uploaddraw(notestitle, notestext, notespdfid, downloadUrl)
//                    save_note(binding, context, checknote,
//                        selectedImageUri!!).savenote(notestitle, notestext, notespdfid, downloadUrl, notesdrawing)
                }
                if (progressDialog.isShowing) progressDialog.dismiss()
            }
        }.addOnFailureListener {
            if (progressDialog.isShowing) progressDialog.dismiss()
        }
    }

}