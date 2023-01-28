package com.example.pdfnoter.uploadpdf

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pdfnoter.Activities.ProfileActivity
import com.example.pdfnoter.R
import com.example.pdfnoter.databinding.FragmentListBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    val storage = FirebaseStorage.getInstance()
    var firestore = FirebaseFirestore.getInstance()
    lateinit var recyclerView: RecyclerView
    private lateinit var PdfListLinkmodel: ArrayList<Listdata>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root
        PdfListLinkmodel = ArrayList()

        binding.uploadpdf.setOnClickListener {
            selectPdf()
        }

        getpdfs()
        profile()

        return view
    }

    private fun getpdfs() {

        val tsLong = System.currentTimeMillis()

        val currentuser = FirebaseAuth.getInstance().currentUser!!.uid.toString()
        val collectionReference =
            firestore.collection("pdfs").document(currentuser).collection("Pdfs").orderBy("timestamp", Query.Direction.DESCENDING)

        collectionReference.addSnapshotListener { value, error ->
            if (value == null || error != null) {
                Toast.makeText(activity, "Error fetching data", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            Log.d("DATA", value.toObjects(Listdata::class.java).toString())
            PdfListLinkmodel.clear()
            PdfListLinkmodel.addAll(value.toObjects(Listdata::class.java))

            recyclerView = binding.recyclerview
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.adapter = ListpdfAdapter(PdfListLinkmodel)

            recyclerView.visibility = View.VISIBLE
        }
    }

    fun profile(){
        binding.profile.setOnClickListener {
            val intent = Intent(activity, ProfileActivity::class.java)
            requireActivity().startActivity(intent)
            requireActivity().finish()
        }
    }

    fun uploadPdf(pdfFile: Uri, identifier: String) {
        val progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("uploading pdf.....")
        progressDialog.setCancelable(false)
        progressDialog.show()
        // Create a storage reference
        val pdfRef = storage.reference.child("pdfs/$identifier.pdf")

        // Upload the file
        val uploadTask = pdfRef.putFile(pdfFile)

        // Listen for completion of the upload
        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Get the download URL of the file
                pdfRef.downloadUrl.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val pdfUrl = task.result.toString()
                        Log.d("pdfurl", pdfUrl)

                        val tsLong = System.currentTimeMillis()
                        val ts : Long = tsLong
                        val pdfdate: String =DateFormat.format("mm-dd-yyyy", ts).toString()
                        val pdfnew = DocumentFile.fromSingleUri(requireActivity(), pdfFile)
                        val pdfFileName = pdfnew?.name.toString()


                        val pdfData = hashMapOf("pdfurl" to pdfUrl, "pdfdate" to pdfdate, "timestamp" to tsLong, "pdfname" to pdfFileName)
                        val currentuser = FirebaseAuth.getInstance().currentUser!!.uid.toString()
                        firestore.collection("pdfs").document(currentuser).collection("Pdfs").add(pdfData)
                            .addOnSuccessListener {
                                Log.d("hey", "Added document with ID ${it}")
                                Toast.makeText(activity, "added pdf", Toast.LENGTH_SHORT).show()
                                if (progressDialog.isShowing) progressDialog.dismiss()
                            }
                            .addOnFailureListener { exception ->
                                Log.w(ContentValues.TAG, "Error adding document $exception")
                                Toast.makeText(activity, "failed", Toast.LENGTH_SHORT).show()

                                if (progressDialog.isShowing) progressDialog.dismiss()
                            }
                    }

                    if (progressDialog.isShowing) progressDialog.dismiss()
                }
            }
        }
    }

    private fun selectPdf() {
        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
        pdfIntent.type = "application/pdf"
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(pdfIntent, 12)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // For loading PDF
        when (requestCode) {
            12 -> if (resultCode == AppCompatActivity.RESULT_OK) {

                val pdfUri = data?.data!!
                val uri: Uri = data.data!!

                Log.d("hi", "$uri")

                val timestamp = System.currentTimeMillis().toString()

                uploadPdf(uri, timestamp)

            }
        }
    }
}