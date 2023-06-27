package com.project.cyberreport

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import java.lang.NullPointerException

class LoginAct : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        try {
            this.supportActionBar!!.hide()
        }
        catch (e: NullPointerException) {
        }

        auth = FirebaseAuth.getInstance()
        val progressDialog: ProgressDialog = ProgressDialog(this)
        val logButton: Button = findViewById(R.id.logButton)
        val logRegText: TextView = findViewById(R.id.logRegText)

        logButton.setOnClickListener {

            val logEmail: EditText? = findViewById(R.id.logEmail)
            val logPassword: EditText? = findViewById(R.id.logPassword)

            verify(logEmail?.text.toString(), logPassword?.text.toString())
            progressDialog.dismiss()
        }

        logRegText.setOnClickListener {
            finish()
            intent = Intent(this, RegisterAct::class.java)
            startActivity(intent)
        }

    }

    private fun verify(useremail: String, userpassword: String ){
        val progressDialog: ProgressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait...")
        progressDialog.show()
        progressDialog.setCancelable(false)

        if (useremail.isEmpty() || userpassword.isEmpty()){
            progressDialog.dismiss()
            Toast.makeText(this, "Please input data in each field.", Toast.LENGTH_SHORT).show()
        } else{
            auth.signInWithEmailAndPassword(useremail, userpassword).addOnCompleteListener (this) {
                if (it.isSuccessful){
                    progressDialog.dismiss()
                    finish()
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                    intent = Intent(this, Content::class.java)
                    startActivity(intent)
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Incorrect email or password.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Do you really want to exit the app?")
        builder.setPositiveButton("Yes") { DialogInterface, i: Int ->
            finish()
        }
        builder.setNegativeButton("No"){ DialogInterface, i: Int ->
        }
        builder.show()
        builder.setCancelable(false)
    }

}