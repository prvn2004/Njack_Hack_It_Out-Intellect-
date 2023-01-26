package com.example.pdfnoter.notes

import android.app.Dialog
import android.content.Context
import android.util.TypedValue
import android.widget.ImageView
import com.example.pdfnoter.R
import com.kyanogen.signatureview.SignatureView

class drawing_parameters(context: Context) {

    var context: Context = context

    fun changecolor(dialog: Dialog, signatureView: SignatureView){

        dialog.findViewById<ImageView>(R.id.marker).setOnClickListener {
            val f = 30 * 1f
            signatureView.penSize = f

            val imageview = dialog.findViewById<ImageView>(R.id.marker)
            changesize(imageview, 35)
            val imageview2 = dialog.findViewById<ImageView>(R.id.pen)
            changesize(imageview2, 25)
            val imageview3 = dialog.findViewById<ImageView>(R.id.eraser)
            changesize(imageview3, 25)

        }
        dialog.findViewById<ImageView>(R.id.pen).setOnClickListener {
            val f = 10 * 1f
            signatureView.penSize = f
            val imageview = dialog.findViewById<ImageView>(R.id.pen)
            changesize(imageview, 35)
            val imageview2 = dialog.findViewById<ImageView>(R.id.marker)
            changesize(imageview2, 25)
            val imageview3 = dialog.findViewById<ImageView>(R.id.eraser)
            changesize(imageview3, 25)
        }
        dialog.findViewById<ImageView>(R.id.eraser).setOnClickListener {
            signatureView.clearCanvas()
            val imageview = dialog.findViewById<ImageView>(R.id.marker)
            changesize(imageview, 25)
            val imageview2 = dialog.findViewById<ImageView>(R.id.eraser)
            changesize(imageview2, 40)
            val imageview3 = dialog.findViewById<ImageView>(R.id.pen)
            changesize(imageview3, 25)
        }

        dialog.findViewById<ImageView>(R.id.yellow).setOnClickListener {
            signatureView.penColor = context.resources.getColor(R.color.color3)
            var imageview = dialog.findViewById<ImageView>(R.id.yellow)
            changesize(imageview, 40)

            var imageview2 = dialog.findViewById<ImageView>(R.id.green)
            var imageview3 = dialog.findViewById<ImageView>(R.id.blue)
            var imageview4= dialog.findViewById<ImageView>(R.id.purple)
            changesize(imageview2, 30)
            changesize(imageview3, 30)
            changesize(imageview4, 30)
        }
        dialog.findViewById<ImageView>(R.id.blue).setOnClickListener {
            signatureView.penColor = context.resources.getColor(R.color.color1)
            var imageview = dialog.findViewById<ImageView>(R.id.blue)
            changesize(imageview, 40)

            var imageview2 = dialog.findViewById<ImageView>(R.id.green)
            var imageview3 = dialog.findViewById<ImageView>(R.id.yellow)
            var imageview4= dialog.findViewById<ImageView>(R.id.purple)
            changesize(imageview2, 30)
            changesize(imageview3, 30)
            changesize(imageview4, 30)



        }
        dialog.findViewById<ImageView>(R.id.green).setOnClickListener {
            signatureView.penColor = context.resources.getColor(R.color.color2)
            var imageview = dialog.findViewById<ImageView>(R.id.green)
            changesize(imageview, 40)

            var imageview2 = dialog.findViewById<ImageView>(R.id.blue)
            var imageview3 = dialog.findViewById<ImageView>(R.id.yellow)
            var imageview4= dialog.findViewById<ImageView>(R.id.purple)
            changesize(imageview2, 30)
            changesize(imageview3, 30)
            changesize(imageview4, 30)

        }
        dialog.findViewById<ImageView>(R.id.purple).setOnClickListener {
            signatureView.penColor = context.resources.getColor(R.color.color4)
            var imageview = dialog.findViewById<ImageView>(R.id.purple)
            changesize(imageview, 40)

            var imageview2 = dialog.findViewById<ImageView>(R.id.green)
            var imageview3 = dialog.findViewById<ImageView>(R.id.yellow)
            var imageview4= dialog.findViewById<ImageView>(R.id.blue)
            changesize(imageview2, 30)
            changesize(imageview3, 30)
            changesize(imageview4, 30)

        }
    }

    fun changesize(imageview: ImageView, size: Int){
        val layoutParams = imageview.layoutParams
        val f = size * 1f
        val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, f, context.resources.displayMetrics).toInt()
        layoutParams.width = width
        layoutParams.height = width
        imageview.layoutParams = layoutParams
    }
}