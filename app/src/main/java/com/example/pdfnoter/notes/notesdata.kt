package com.example.pdfnoter.notes

import com.google.firebase.firestore.DocumentId

data class notesdata(
    var notestitle: String = "",
    var notesdate: String = "",
    var notestext: String = "",
    var notespdfid: String = "",
    var timestamp: Long = 0,
    var notesimage: String = "",
    var notesdrawing: String = "",
    @DocumentId
    val documentId: String = ""
) {
    fun getmynotestitle(): String {
        return notestitle
    }

    fun getmynotespdfid(): String {
        return notespdfid
    }

    fun getmynotesdate(): String {
        return notesdate
    }

    fun getmynotestext(): String {
        return notestext
    }

    fun getdocid(): String {
        return documentId
    }

    fun getmynotesimage(): String {
        return notesimage
    }
    fun getmynotesdrawing(): String {
        return notesdrawing
    }
}
