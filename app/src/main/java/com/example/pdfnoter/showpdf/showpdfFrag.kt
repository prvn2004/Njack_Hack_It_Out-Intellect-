package com.example.pdfnoter.showpdf

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pdfnoter.databinding.FragmentShowpdfBinding
import com.example.pdfnoter.notes.*
import com.example.pdfnoter.uploadpdf.Listdata
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class showpdfFrag : Fragment(), OnClickListener {

    private lateinit var binding: FragmentShowpdfBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    val storage = FirebaseStorage.getInstance()
    var firestore = FirebaseFirestore.getInstance()
    lateinit var recyclerView: RecyclerView
    private lateinit var NotesListLinkmodel: ArrayList<notesdata>
    private val PICK_IMAGE = 1
    var checknote : Int = 0
    var pdfid1: String = ""
    var docid1: String =""
    var selectedImageUri : Uri? = Uri.parse("")
    var imageurl: String = ""
    private var mDefaultColor = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowpdfBinding.inflate(inflater, container, false)
        val view = binding.root

        NotesListLinkmodel = ArrayList()
        drawerLayout = binding.drawerLayout

        val bottomSheet = binding.eventBottomSheet
        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.peekHeight = 400
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        mDefaultColor =0


        val pdfurl = requireArguments().getString("pdfurl")
        get_pdf(binding, requireActivity()).getfile(pdfurl.toString())
        opendrawer()
        binding.notestext.movementMethod = LinkMovementMethod.getInstance()

        binding.insertImage.setOnClickListener {
            selectImage()
        }

        binding.newnote.setOnClickListener {
            binding.eventBottomSheet.visibility = View.VISIBLE
            drawerLayout.closeDrawer(Gravity.RIGHT)
        }

        binding.backSave.setOnClickListener {
            uploadnotes()
        }

        binding.insertDrawing.setOnClickListener {
            drawing_dialog(binding, requireActivity()).opendraw()
        }

        binding.upload.setOnClickListener {
         uploadnotes()
        }

        return view
    }

    fun uploadnotes(){
        val notestitle= binding.notestitle.text.toString()
        val notestext = binding.notestext.text.toString()
        if (notestext.isNullOrEmpty() && notestitle.isNullOrEmpty()) {
            binding.eventBottomSheet.visibility = View.GONE
            binding.notestext.text.clear()
            binding.notestitle.text.clear()
            binding.insertedImage.setImageResource(0)
            selectedImageUri = Uri.parse("")
        }
        else if (notestext.isNotEmpty() || notestitle.isNotEmpty()) {
            if (checknote == 0){
                val docid = requireArguments().getString("docid").toString()
                if (selectedImageUri.toString().isNullOrEmpty()){
                    save_note(binding, requireActivity(), checknote,
                        selectedImageUri!!).savenote(notestitle, notestext, docid, "", "")
                }else{
                    upload_image(binding, requireActivity(), checknote,
                        selectedImageUri!!).uploadimage(notestitle, notestext, docid, docid1.toString(), pdfid1)
                }
            }else if(checknote == 1){
                if (selectedImageUri.toString().isNullOrEmpty()){
                    edit_notes(binding, requireActivity()).editdoc(notestitle, notestext, imageurl, docid1, pdfid1)
                }else{
                    val docid = requireArguments().getString("docid").toString()
                    upload_image(binding, requireActivity(), checknote,
                        selectedImageUri!!).uploadimage(notestitle, notestext, docid, docid1.toString(), pdfid1)
                }
            }
        }else{
            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
        }
    }

//    fun uploaddraw(bitmap: Bitmap){
//        val baos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//        val data = baos.toByteArray()
//
//        val uploadTask = storageRef.putBytes(data)
//    }

    override fun onClick(notestitle: String, notestext: String, notesimage: String, docid: String, pdfid: String) {
        binding.eventBottomSheet.visibility = View.VISIBLE
        drawerLayout.closeDrawer(Gravity.RIGHT)
        binding.notestitle.setText(notestitle)
        binding.notestext.setText(notestext)
        checknote =1
        pdfid1= pdfid.toString()
        docid1 = docid.toString()
        imageurl = notesimage

        highlight_url(requireActivity()).highlightUrls(binding.notestext)

        binding.insertedImage.visibility = View.VISIBLE

        Glide.with(this)
            .load(notesimage)
            .into(binding.insertedImage)
    }

    private fun selectImage() {
        binding.insertedImage.setImageResource(0)
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            binding.insertedImage.visibility = View.VISIBLE
            binding.insertedImage.setImageURI(selectedImageUri)
        }
    }

    fun opendrawer() {
        actionBarToggle = ActionBarDrawerToggle(activity, drawerLayout, 0, 0)
        drawerLayout.addDrawerListener(actionBarToggle)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarToggle.syncState()

        binding.AllNotes.setOnClickListener {
            drawerLayout.openDrawer(Gravity.RIGHT)
            val docid = requireArguments().getString("docid")
            show_allnote(binding, requireActivity()).showAllNotes(docid.toString(), this)
        }
    }
}

