package com.example.pdfnoter.showpdf

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.pdfnoter.R
import com.example.pdfnoter.databinding.ActivityShowpdfBinding

class showpdfActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowpdfBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityShowpdfBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)

        val pdfurl = intent.extras?.getString("pdfurl")
        val docid = intent.extras?.getString("docid")
        Log.d("url", "$pdfurl + $docid")

        setContentView(view)

        setFragment(showpdfFrag(), pdfurl.toString(), docid.toString())
    }
    private fun setFragment(fragment: Fragment, url: String, docid: String) {
        val fram =  supportFragmentManager.beginTransaction()
        val args = Bundle()
        args.putString("pdfurl", url)
        args.putString("docid", docid)
        fragment.setArguments(args)
        fram.replace(R.id.show_container, fragment)
        fram.commit()
    }
}