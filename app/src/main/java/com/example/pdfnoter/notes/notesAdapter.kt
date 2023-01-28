package com.example.pdfnoter.notes

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pdfnoter.R
import com.example.pdfnoter.databinding.NoteslistitemBinding
import com.example.pdfnoter.databinding.PdflistitemBinding
import com.example.pdfnoter.showpdf.showpdfActivity
import com.example.pdfnoter.showpdf.showpdfFrag
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

interface OnClickListener {
    fun onClick(
        notestitle: String,
        notestext: String,
        notesimage: String,
        drawingImage: String,
        docid: String,
        pdfid: String
    )
}

class notesAdapter(
    private var LinkList: ArrayList<notesdata>,
    private val listener: OnClickListener
) :

    RecyclerView.Adapter<notesAdapter.MyViewHolder>() {
// ------------------------------------------------------------------------------------------------------------------------------------------

    private lateinit var binding: NoteslistitemBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    var totalshare: Long = 0

    //-------------------------------------------------------------------------------------------------------------------------------------------------

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): notesAdapter.MyViewHolder {

        firestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()


        binding =
            NoteslistitemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return notesAdapter.MyViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(
        holder: notesAdapter.MyViewHolder,
        position: Int
    ) {
        val Link = LinkList[position]
        holder.friction(Link, position, LinkList)

        binding.clicking.setOnClickListener {
            listener.onClick(
                LinkList.get(position).getmynotestitle(),
                LinkList.get(position).getmynotestext(),
                LinkList.get(position).getmynotesimage(),
                LinkList.get(position).getmynotesdrawing(),
                LinkList.get(position).getdocid(),
                LinkList.get(position).getmynotespdfid()
            )
        }



        Glide.with(holder.itemView.context)
            .load(LinkList.get(position).getmynotesimage()).placeholder(R.drawable.placeholder)
            .into(binding.image)

    }


//    private fun fragmentJump(context: Context, Docid: String) {
//        val  mFragment = showpdfFrag()
//        switchContent(R.id.container, mFragment, context, Docid)
//    }
//
//    private fun switchContent(id: Int, fragment: Fragment, context: Context, Docid: String) {
//        if (context is MainActivity) {
//            val enotesActivity = context as MainActivity
//            val frag: Fragment = fragment
//            enotesActivity.switchContent(id, frag, Docid)
//        }
//    }

    override fun getItemCount(): Int {
        return LinkList.size
    }

    class MyViewHolder(
        ItemViewBinding: NoteslistitemBinding,
    ) :
        RecyclerView.ViewHolder(ItemViewBinding.root) {

        private val binding = ItemViewBinding

        @SuppressLint("SetTextI18n")
        fun friction(Link: notesdata, position: Int, list: ArrayList<notesdata>) {

            val context = itemView.getContext()

            val num = position + 1
            binding.mynote.text = Link.getmynotestitle()

        }
    }
}
