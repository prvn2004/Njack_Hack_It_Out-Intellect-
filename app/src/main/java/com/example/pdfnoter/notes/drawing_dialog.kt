package com.example.pdfnoter.notes

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.pdfnoter.R
import com.example.pdfnoter.databinding.FragmentShowpdfBinding
import com.kyanogen.signatureview.SignatureView

class drawing_dialog(private var binding: FragmentShowpdfBinding, var context: Context) {

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
            binding.insertedDraw.visibility = View.VISIBLE
            binding.insertedDraw.setImageBitmap(bitmap)
            dialog.cancel()
        }
    }
}