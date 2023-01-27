package com.example.pdfnoter.notes

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.preference.PreferenceManager
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.pdfnoter.R
import com.example.pdfnoter.databinding.FragmentShowpdfBinding
import com.kyanogen.signatureview.SignatureView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class drawing_dialog(private var binding: FragmentShowpdfBinding, var context: Context) {

    public var bitmap_str : String= ""

    fun opendraw() {
        val dialog = Dialog(context)

        dialog.setContentView(R.layout.draw_layout);
        dialog.getWindow()
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow()?.getAttributes()!!.windowAnimations = R.style.animation;

        dialog.show();

        val signatureView = dialog.findViewById(R.id.signature_view) as SignatureView

        drawing_parameters(context).changecolor(dialog, signatureView)

        setdraw(dialog, signatureView)
    }

    fun setdraw(dialog: Dialog, signatureView: SignatureView) {
        dialog.findViewById<Button>(R.id.save).setOnClickListener {
            val bitmap: Bitmap = signatureView.getSignatureBitmap()
//            val byteArrayOutputStream = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
//            val byteArray = byteArrayOutputStream.toByteArray()
//            bitmap_str= Base64.encodeToString(byteArray, Base64.DEFAULT)
//            Log.d("hello lodu", bitmap_str)
            val bitmapFile = File.createTempFile("tempBitmap", ".jpg", context.cacheDir)
            val outputStream = FileOutputStream(bitmapFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            val bitmapUri = Uri.fromFile(bitmapFile).toString()
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = preferences.edit()
            editor.putString("bitmap_uri", bitmapUri)
            editor.apply()

            binding.insertedDraw.visibility = View.VISIBLE
            binding.insertedDraw.setImageBitmap(bitmap)
            dialog.cancel()
        }
    }
}