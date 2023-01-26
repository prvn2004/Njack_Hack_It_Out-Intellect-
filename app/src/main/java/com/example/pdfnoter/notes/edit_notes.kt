package com.example.pdfnoter.notes

import android.app.ProgressDialog
import android.content.Context
import android.view.View
import com.example.pdfnoter.databinding.FragmentShowpdfBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class edit_notes(private var binding: FragmentShowpdfBinding, var context: Context) {

    var firestore = FirebaseFirestore.getInstance()

    fun editdoc(
        notestitle: String,
        notestext: String,
        notesimage: String,
        docid: String,
        pdfid: String
    ) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("updating note......")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val currentuser = FirebaseAuth.getInstance().currentUser!!.uid.toString()
        val reference = firestore.collection("pdfs").document(currentuser).collection("Pdfs")
            .document(pdfid).collection("Notes").document(docid)
        reference.update("notestitle", notestitle)
        reference.update("notesimage", notesimage)
        reference.update("notestext", notestext).addOnSuccessListener {
            if (progressDialog.isShowing) progressDialog.dismiss()
            binding.eventBottomSheet.visibility = View.GONE
            binding.notestext.text.clear()
            binding.notestitle.text.clear()
            binding.insertedImage.setImageResource(0)

        }
    }
}