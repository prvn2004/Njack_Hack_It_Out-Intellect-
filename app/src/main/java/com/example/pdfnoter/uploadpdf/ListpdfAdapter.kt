package com.example.pdfnoter.uploadpdf

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pdfnoter.R
import com.example.pdfnoter.databinding.PdflistitemBinding
import com.example.pdfnoter.showpdf.showpdfActivity
import com.example.pdfnoter.showpdf.showpdfFrag
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ListpdfAdapter(
    private var LinkList: ArrayList<Listdata>
) :

    RecyclerView.Adapter<ListpdfAdapter.MyViewHolder>() {
// ------------------------------------------------------------------------------------------------------------------------------------------

    private lateinit var binding: PdflistitemBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    var totalshare: Long = 0

    //-------------------------------------------------------------------------------------------------------------------------------------------------

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListpdfAdapter.MyViewHolder {

        firestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()


        binding =
            PdflistitemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return ListpdfAdapter.MyViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(
        holder: ListpdfAdapter.MyViewHolder,
        position: Int
    ) {
        val Link = LinkList[position]
        holder.friction(Link, position, LinkList)

        binding.clickbait.setOnClickListener {
           val url = LinkList.get(position).getmypdfurl()
            val docid = LinkList.get(position).getdocid()
            val intent = Intent(holder.itemView.context, showpdfActivity::class.java)
            intent.putExtra("pdfurl", url)
            intent.putExtra("docid", docid)
            holder.itemView.context.startActivity(intent)
        }

    }
//
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
        ItemViewBinding: PdflistitemBinding,
    ) :
        RecyclerView.ViewHolder(ItemViewBinding.root) {

        private val binding = ItemViewBinding
        fun friction(Link: Listdata, position: Int, list: ArrayList<Listdata>) {

            val context = itemView.getContext()

            binding.pdfname.text = Link.getmypdfname().toString()
            binding.pdfdate.text ="•  " + Link.getmypdfdate().toString()

        }
    }
}
