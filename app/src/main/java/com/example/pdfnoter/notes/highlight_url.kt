package com.example.pdfnoter.notes

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.example.pdfnoter.R

class highlight_url (context: Context) {

    var context: Context = context

    public fun highlightUrls(editText: EditText) {
        val text = editText.text
        val spannableString = SpannableString(text)

        val matcher = Patterns.WEB_URL.matcher(text)
        while (matcher.find()) {
            val start = matcher.start()
            val end = matcher.end()
            val url = matcher.group()

            spannableString.setSpan(
                ForegroundColorSpan(context.resources.getColor(R.color.Blue)),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                object : URLSpan(url) {
                    override fun onClick(widget: View) {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(browserIntent)
                    }
                },
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        editText.setText(spannableString, TextView.BufferType.SPANNABLE)
    }

}