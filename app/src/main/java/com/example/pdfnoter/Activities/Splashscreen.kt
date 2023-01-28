package com.example.pdfnoter.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import com.example.pdfnoter.databinding.ActivitySplashscreenBinding
import com.example.pdfnoter.uploadpdf.ListPdfActivity
import com.google.firebase.auth.FirebaseAuth

class Splashscreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashscreenBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        val view = binding.root
        auth = FirebaseAuth.getInstance()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        actionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            checkuser()
        }, 1000)

        setContentView(view)
    }
    private fun checkuser() {
        val currentuser = auth.currentUser
        if (currentuser != null) {
            val intent = Intent(this, ListPdfActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}