package com.example.pdfnoter.uploadpdf

import com.google.firebase.firestore.DocumentId

data class Listdata(
    var pdfname: String = "",
    var pdfdate: String = "",
    var pdfurl: String = "",
    var timestamp: Long = 0,

    @DocumentId
    val documentId: String = ""
) {
    fun getmypdfname(): String {
        return pdfname
    }

    fun getmypdfdate(): String {
        return pdfdate
    }

    fun getmypdfurl(): String {
        return pdfurl
    }
    fun getdocid(): String{
        return documentId
    }
}
