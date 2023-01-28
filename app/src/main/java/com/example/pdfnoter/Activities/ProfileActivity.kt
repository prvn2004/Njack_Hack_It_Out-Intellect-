package com.example.pdfnoter.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import com.example.pdfnoter.R
import com.example.pdfnoter.databinding.ActivityProfileBinding
import com.example.pdfnoter.uploadpdf.ListPdfActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.wallet.callback.OnCompleteListener

class ProfileActivity : AppCompatActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = this.let { GoogleSignIn.getClient(it, gso) }

        binding.signout.setOnClickListener {
            signOut()
        }

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        val personName = acct?.displayName.toString()
        val personEmail = acct?.email.toString()

        binding.Name.setText(personName)
        binding.email.setText(personEmail)

        binding.threeLines.setOnClickListener{
            val intent = Intent(this, ListPdfActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    private fun signOut() {
//        val progressDialog = ProgressDialog(this)
//        progressDialog.setMessage("SigningOut Please Wait")
//        progressDialog.setCancelable(false)
//        progressDialog.show()
        this.let {
            mGoogleSignInClient.signOut().addOnCompleteListener {
                val preferences = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = preferences.edit()
                editor.clear()
                editor.apply()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                this.finish()
            }

        }
    }
}
