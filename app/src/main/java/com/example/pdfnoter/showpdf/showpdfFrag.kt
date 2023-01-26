package com.example.pdfnoter.showpdf

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.*
import android.text.format.DateFormat
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.util.Log
import android.util.Patterns
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.pdfnoter.R
import com.example.pdfnoter.databinding.FragmentShowpdfBinding
import com.example.pdfnoter.notes.OnClickListener
import com.example.pdfnoter.notes.notesAdapter
import com.example.pdfnoter.notes.notesdata
import com.example.pdfnoter.uploadpdf.Listdata
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kyanogen.signatureview.SignatureView
import java.io.ByteArrayOutputStream
import java.io.File

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


        getfile()
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
            opendraw()
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
                    savenote(notestitle, notestext, docid, "")
                }else{
                    uploadimage(notestitle, notestext, docid)
                }
            }else if(checknote == 1){
                if (selectedImageUri.toString().isNullOrEmpty()){
                    editdoc(notestitle, notestext, imageurl, docid1, pdfid1)
                }else{
                    val docid = requireArguments().getString("docid").toString()
                    uploadimage(notestitle, notestext, docid)
                }
            }
        }else{
            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    fun opendraw(){
        var dialog= Dialog(requireActivity())

        dialog.setContentView(R.layout.draw_layout);
        dialog.getWindow()
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow()?.getAttributes()!!.windowAnimations = R.style.animation;

        dialog.show();

        val signatureView = dialog.findViewById(R.id.signature_view) as SignatureView

        changecolor(dialog, signatureView)

        setdraw(dialog, signatureView)


//        val Deletebutton: Button = dialog.findViewById(R.id.button)
//        val EditButton: Button = dialog.findViewById(R.id.editbutton)
    }

    fun uploaddraw(bitmap: Bitmap){
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
    }

    fun setdraw(dialog: Dialog, signatureView: SignatureView){
        dialog.findViewById<Button>(R.id.save).setOnClickListener{
            val bitmap: Bitmap = signatureView.getSignatureBitmap()
            binding.insertedDraw.visibility = View.VISIBLE
            binding.insertedDraw.setImageBitmap(bitmap)
            dialog.cancel()
        }
    }

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
            signatureView.penColor = resources.getColor(R.color.color3)
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
            signatureView.penColor = resources.getColor(R.color.color1)
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
            signatureView.penColor = resources.getColor(R.color.color2)
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
            signatureView.penColor = resources.getColor(R.color.color4)
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
        val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, f, resources.displayMetrics).toInt()
        layoutParams.width = width
        layoutParams.height = width
        imageview.layoutParams = layoutParams
    }


    private fun highlightUrls(editText: EditText) {
        val text = editText.text
        val spannableString = SpannableString(text)

        val matcher = Patterns.WEB_URL.matcher(text)
        while (matcher.find()) {
            val start = matcher.start()
            val end = matcher.end()
            val url = matcher.group()

            spannableString.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.Blue)),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                object : URLSpan(url) {
                    override fun onClick(widget: View) {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(browserIntent)
                    }
                },
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        editText.setText(spannableString, TextView.BufferType.SPANNABLE)
    }

    private fun editdoc(
        notestitle: String,
        notestext: String,
        notesimage: String,
        docid: String,
        pdfid: String
    ) {
        val progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("updating note......")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val currentuser = FirebaseAuth.getInstance().currentUser!!.uid.toString()
        val reference = firestore.collection("pdfs").document(currentuser).collection("Pdfs")
            .document(pdfid).collection("Notes").document(docid)
       reference.update("notestitle", notestitle)
        reference.update("notesimage", notesimage)
        reference.update("notestext", notestext).addOnSuccessListener {
            if (progressDialog.isShowing) progressDialog.dismiss()
            binding.eventBottomSheet.visibility = View.GONE
            binding.notestext.text.clear()
            binding.notestitle.text.clear()
            binding.insertedImage.setImageResource(0)

        }
    }

    override fun onClick(notestitle: String, notestext: String, notesimage: String, docid: String, pdfid: String) {
        binding.eventBottomSheet.visibility = View.VISIBLE
        drawerLayout.closeDrawer(Gravity.RIGHT)
        binding.notestitle.setText(notestitle)
        binding.notestext.setText(notestext)
        checknote =1
        pdfid1= pdfid.toString()
        docid1 = docid.toString()
        imageurl = notesimage

        highlightUrls(binding.notestext)

        binding.insertedImage.visibility = View.VISIBLE

        Glide.with(this)
            .load(notesimage)
            .into(binding.insertedImage)



    }

    fun savenote(notestitle: String, notestext: String, notespdfid: String, notesimage: String) {
        val progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("updating note......")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val tsLong = System.currentTimeMillis()
        val ts: Long = tsLong
        val notesdate: String = DateFormat.format("mm-dd-yyyy", ts).toString()

        val pdfData = hashMapOf(
            "notestitle" to notestitle,
            "notestext" to notestext,
            "timestamp" to ts,
            "notespdfid" to notespdfid,
            "notesdate" to notesdate,
            "notesimage" to notesimage
        )
        val currentuser = FirebaseAuth.getInstance().currentUser!!.uid.toString()
        firestore.collection("pdfs").document(currentuser).collection("Pdfs").document(notespdfid)
            .collection("Notes").add(pdfData)
            .addOnSuccessListener {
                Log.d("hey", "Added document with ID ${it}")
                Toast.makeText(activity, "updated note", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
                binding.eventBottomSheet.visibility = View.GONE
                binding.notestext.text.clear()
                binding.notestitle.text.clear()
                binding.insertedImage.setImageResource(0)
                selectedImageUri = Uri.parse("")

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error adding document $exception")
                Toast.makeText(activity, "failed", Toast.LENGTH_SHORT).show()

                if (progressDialog.isShowing) progressDialog.dismiss()
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            binding.insertedImage.visibility = View.VISIBLE
            binding.insertedImage.setImageURI(selectedImageUri)
        }
    }

    private fun uploadimage(notestitle: String, notestext: String, notespdfid: String){
        val progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("uploading image......")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val tsLong = System.currentTimeMillis()
        val ts: Long = tsLong
        val storageRef = storage.reference
        val imageRef = storageRef.child("images/${selectedImageUri?.lastPathSegment}")
        val uploadTask = imageRef.putFile(selectedImageUri!!)

        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                val downloadUrl = it.toString()
                if (checknote == 1){
                    editdoc(notestitle, notestext,downloadUrl, docid1, pdfid1)
                }else{
                    savenote(notestitle, notestext, notespdfid, downloadUrl)
                }
                if (progressDialog.isShowing) progressDialog.dismiss()
            }
        }.addOnFailureListener {

        }

    }

    private fun selectImage() {
        binding.insertedImage.setImageResource(0)
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)

    }

    fun showAllNotes() {
        val tsLong = System.currentTimeMillis()

        val currentuser = FirebaseAuth.getInstance().currentUser!!.uid.toString()
        val docid = requireArguments().getString("docid")
        val collectionReference =
            firestore.collection("pdfs").document(currentuser).collection("Pdfs")
                .document(docid.toString()).collection("Notes")

        collectionReference.addSnapshotListener { value, error ->
            if (value == null || error != null) {
                Toast.makeText(activity, "Error fetching data", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            Log.d("DATA", value.toObjects(Listdata::class.java).toString())
            NotesListLinkmodel.clear()
            NotesListLinkmodel.addAll(value.toObjects(notesdata::class.java))

            recyclerView = binding.notesRecyclerView
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.adapter = notesAdapter(NotesListLinkmodel, this)

            recyclerView.visibility = View.VISIBLE
        }
    }

    fun opendrawer() {
        actionBarToggle = ActionBarDrawerToggle(activity, drawerLayout, 0, 0)
        drawerLayout.addDrawerListener(actionBarToggle)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarToggle.syncState()

        binding.AllNotes.setOnClickListener {
            drawerLayout.openDrawer(Gravity.RIGHT)
            showAllNotes()
        }
    }

    fun onSupportNavigateUp(): Boolean {
        drawerLayout.openDrawer(navView)
        return true
    }

    fun getfile() {
        val pdfurl = requireArguments().getString("pdfurl")
        val url: Uri = Uri.parse(pdfurl)

        Glide.with(this)
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

