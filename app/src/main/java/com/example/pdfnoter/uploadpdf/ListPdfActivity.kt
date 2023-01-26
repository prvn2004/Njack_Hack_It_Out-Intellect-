package com.example.pdfnoter.uploadpdf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.pdfnoter.R
import com.example.pdfnoter.databinding.ActivityListPdfBinding

class ListPdfActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListPdfBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityListPdfBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)

        setFragment(ListFragment())
    }
    private fun setFragment(fragment: Fragment) {
        val fram =  supportFragmentManager.beginTransaction()
        fram.replace(R.id.container, fragment)
        fram.commit()
    }
}