package com.example.pdfnoter.notes

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pdfnoter.databinding.FragmentShowpdfBinding
import com.example.pdfnoter.uploadpdf.Listdata
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class show_allnote(private var binding: FragmentShowpdfBinding, var context: Context) {

    var firestore = FirebaseFirestore.getInstance()
    private lateinit var NotesListLinkmodel: ArrayList<notesdata>
    lateinit var recyclerView: RecyclerView

    fun showAllNotes(docid:String, clickListener: OnClickListener) {
        NotesListLinkmodel = ArrayList()
        val tsLong = System.currentTimeMillis()

        val currentuser = FirebaseAuth.getInstance().currentUser!!.uid.toString()
        val collectionReference =
            firestore.collection("pdfs").document(currentuser).collection("Pdfs")
                .document(docid.toString()).collection("Notes")

        collectionReference.addSnapshotListener { value, error ->
            if (value == null || error != null) {
                Toast.makeText(context, "Error fetching data", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            Log.d("DATA", value.toObjects(Listdata::class.java).toString())
            NotesListLinkmodel.clear()
            NotesListLinkmodel.addAll(value.toObjects(notesdata::class.java))

            recyclerView = binding.notesRecyclerView
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = notesAdapter(NotesListLinkmodel, clickListener)

            recyclerView.visibility = View.VISIBLE
        }
    }

}