package com.example.pdfnoter.notes

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.preference.PreferenceManager
import android.util.Base64.DEFAULT
import android.util.Base64.decode
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.pdfnoter.databinding.FragmentShowpdfBinding
import com.google.android.gms.common.util.Base64Utils.decode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.lang.Byte.decode
import java.util.*

class upload_drawing(private var binding: FragmentShowpdfBinding, var context: Context, var selectedImageUri: Uri, var checknote:Int) {

    val storage = FirebaseStorage.getInstance()

    fun uploaddraw(notestitle: String, notestext: String, notespdfid: String, notesimage: String){
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("uploading drawing......")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val bitmap_str = preferences.getString("bitmap_uri", "").toString()

        val storageRef = storage.reference

        val tsLong = System.currentTimeMillis()
        val ts: Long = tsLong
        val currentuser = FirebaseAuth.getInstance().currentUser!!.uid.toString()

        val draw_uri :Uri = Uri.parse(bitmap_str)
        val imageRef = storageRef.child("images/$currentuser$ts")
        val uploadTask = imageRef.putFile(draw_uri)

        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
               val drawingurl = it.toString()
                save_note(binding, context, checknote,
                    selectedImageUri!!).savenote(notestitle, notestext, notespdfid, notesimage, drawingurl)
                if (progressDialog.isShowing) progressDialog.dismiss()

            }}
        if (progressDialog.isShowing) progressDialog.dismiss()

    }

}