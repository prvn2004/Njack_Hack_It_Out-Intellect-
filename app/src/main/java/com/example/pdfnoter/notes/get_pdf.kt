package com.example.pdfnoter.notes

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.pdfnoter.databinding.FragmentShowpdfBinding
import java.io.File

class get_pdf (private var binding: FragmentShowpdfBinding, var context: Context){

    fun getfile(pdfurl: String) {
        val url: Uri = Uri.parse(pdfurl)

        Glide.with(context)
            .asFile()
            .load(url)
            .into(object : CustomTarget<File>() {
                override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                    binding.pdfView.fromFile(resource)
                        .enableAnnotationRendering(true)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .load()
                    binding.progressBar.visibility = View.GONE
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

}