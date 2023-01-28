package com.example.pdfnoter.notes

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.pdfnoter.databinding.FragmentShowpdfBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class save_note(
    private var binding: FragmentShowpdfBinding,
    var context: Context,
    var checknote: Int,
    var selectedImageUri: Uri
) {

    var firestore = FirebaseFirestore.getInstance()
    private var firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()

    fun savenote(
        notestitle: String,
        notestext: String,
        notespdfid: String,
        notesimage: String,
        notesdrawing: String
    ) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("updating note......")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val tsLong = System.currentTimeMillis()
        val ts: Long = tsLong
        val notesdate: String = DateFormat.format("mm-dd-yyyy", ts).toString()

        val pdfData = hashMapOf(
            "notestitle" to notestitle,
            "notestext" to notestext,
            "timestamp" to ts,
            "notespdfid" to notespdfid,
            "notesdate" to notesdate,
            "notesimage" to notesimage,
            "notesdrawing" to notesdrawing
        )
        val currentuser = FirebaseAuth.getInstance().currentUser!!.uid.toString()
        firestore.collection("pdfs").document(currentuser).collection("Pdfs").document(notespdfid)
            .collection("Notes").add(pdfData)
            .addOnSuccessListener {
                Log.d("hey", "Added document with ID ${it}")
                Toast.makeText(context, "updated note", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
                binding.eventBottomSheet.visibility = View.GONE
                binding.notestext.text.clear()
                binding.notestitle.text.clear()
                binding.insertedImage.setImageResource(0)
                selectedImageUri = Uri.parse("")
            }
            .addOnFailureListener {
                Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()

                if (progressDialog.isShowing) progressDialog.dismiss()
            }
        if (progressDialog.isShowing) progressDialog.dismiss()


    }

    fun deletedoc(notesid: String, pdfid: String, imageur: String, drawingurl: String) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("deleting note......")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val imageurl = imageur
        val currentuser = FirebaseAuth.getInstance().currentUser!!.uid.toString()
        val reference =
            firestore.collection("pdfs").document(currentuser).collection("Pdfs").document(pdfid)
                .collection("Notes")

        if (imageurl.isNotEmpty()) {
            val photoRef: StorageReference = firebaseStorage.getReferenceFromUrl(imageurl)
            photoRef.delete()
        }
        if (drawingurl.isNotEmpty()) {
            val drawref: StorageReference = firebaseStorage.getReferenceFromUrl(drawingurl)
            drawref.delete()
        }

        reference.document(notesid).delete().addOnSuccessListener {
            binding.eventBottomSheet.visibility = View.GONE
            binding.notestext.text.clear()
            binding.notestitle.text.clear()
            binding.insertedImage.setImageResource(0)
            selectedImageUri = Uri.parse("")
        }
        if (progressDialog.isShowing) progressDialog.dismiss()

    }
}