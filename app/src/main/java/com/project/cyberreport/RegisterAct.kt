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
import kotlin.system.exitProcess

class RegisterAct : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        try {
            this.supportActionBar!!.hide()
        }
        catch (e: NullPointerException) {
        }

        val regButton: Button = findViewById(R.id.regButton)
        val regEmail: EditText = findViewById(R.id.regEmail)
        val regPassword: EditText = findViewById(R.id.regPassword)
        val regLoginText: TextView = findViewById(R.id.regLoginText)

        auth = FirebaseAuth.getInstance()

        regButton.setOnClickListener {

            val progressDialog: ProgressDialog = ProgressDialog(this)
            progressDialog.setMessage("Please wait...")
            progressDialog.show()
            progressDialog.setCancelable(false)

            if (verify()){

                val useremail: String = regEmail.text.toString().trim()
                val userpassword: String = regPassword.text.toString().trim()

                auth.createUserWithEmailAndPassword(useremail, userpassword).addOnCompleteListener {
                    if (it.isSuccessful){
                        progressDialog.dismiss()
                        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
                        intent = Intent(this, LoginAct::class.java)
                        startActivity(intent)
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Registration Failed.", Toast.LENGTH_SHORT).show()
                    }
                }

            } else{
                progressDialog.dismiss()
            }

        }

        regLoginText.setOnClickListener {
            finish()
            intent = Intent(this, LoginAct::class.java)
            startActivity(intent)
        }

    }

    private fun verify(): Boolean {
        var result = false
        val regEmailInput: EditText = findViewById(R.id.regEmail)
        val regPasswordInput: EditText = findViewById(R.id.regPassword)

        val email = regEmailInput.text.toString()
        val password = regPasswordInput.text.toString()

            if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please input data in each field.", Toast.LENGTH_SHORT).show()
            }else{
                result = true
                }
            return result
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