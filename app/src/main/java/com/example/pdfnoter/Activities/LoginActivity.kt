package com.example.pdfnoter.Activities

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import com.example.pdfnoter.MainActivity
import com.example.pdfnoter.R
import com.example.pdfnoter.databinding.ActivityLoginBinding
import com.example.pdfnoter.notes.save_local
import com.example.pdfnoter.uploadpdf.ListPdfActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var database: DatabaseReference
    val Req_Code: Int = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root

        super.onCreate(savedInstanceState)
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        binding.loginButton.setOnClickListener {
            signInGoogle()
        }

        checkuser()
        initialisegso()
    }

    private fun initialisegso() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = this.let { GoogleSignIn.getClient(it, gso) }


    }

    private fun checkuser() {
        val currentuser = auth.currentUser
        if (currentuser != null) {
            val intent = Intent(this, ListPdfActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            UpdateUI(account)
        } catch (_: ApiException) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun UpdateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "login success", Toast.LENGTH_SHORT).show()
                val acct = GoogleSignIn.getLastSignedInAccount(this)
                    val personName = acct?.displayName.toString()
                    val personEmail = acct?.email.toString()

                    val preferences = PreferenceManager.getDefaultSharedPreferences(this)
                    val editor = preferences.edit()
                    editor.putString("Email", personEmail)
                    editor.apply()

                    val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
                    save_local(this).save_user(uid)
                    WriteNewUser(personName, personEmail, uid)

                val intent = Intent(this, ListPdfActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun WriteNewUser(personName: String, personEmail: String, uid: String) {
        val fireStoreDatabase = FirebaseFirestore.getInstance()

        val hashMap = hashMapOf<String, Any>(
            "Name" to personName,
            "Email" to personEmail,
            "uid" to uid
        )

        fireStoreDatabase.collection("Users").document(uid)
            .set(hashMap)
            .addOnSuccessListener {
                Log.d("hey", "Added document with ID ${it}")
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error adding document $exception")
            }
    }
}