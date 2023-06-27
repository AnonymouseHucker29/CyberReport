package com.project.cyberreport

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Last : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_last)

        auth = FirebaseAuth.getInstance()
        val welcomeEmailLast: TextView = findViewById(R.id.welcomeEmailLast)
        val user = Firebase.auth.currentUser

        for (profile in user?.providerData!!) {
            val email: String? = profile.email
            welcomeEmailLast.text = "Thank you for submitting your report using our app $email!"
        }

        val exit: Button = findViewById(R.id.exit)
        exit.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Do you really want to sign out?")
            builder.setPositiveButton("Yes") { DialogInterface, i: Int ->
                val progressDialog: ProgressDialog = ProgressDialog(this)
                progressDialog.setMessage("Signing you out...")
                logout()
                progressDialog.dismiss()
            }
            builder.setNegativeButton("No") { DialogInterface, i: Int ->
            }
            builder.show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.appbar, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.appbarlogout -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Do you really want to sign out?")
                builder.setPositiveButton("Yes") { DialogInterface, i: Int ->
                    val progressDialog: ProgressDialog = ProgressDialog(this)
                    progressDialog.setMessage("Signing you out...")
                    logout()
                    progressDialog.dismiss()
                }
                builder.setNegativeButton("No") { DialogInterface, i: Int ->
                }
                builder.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout(){
        val progressDialog: ProgressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait.")
        progressDialog.show()
        progressDialog.setCancelable(false)
        Firebase.auth.signOut()
        finish()
        Toast.makeText(this, "Signed Out.", Toast.LENGTH_SHORT).show()
        intent = Intent(this, LoginAct::class.java)
        startActivity(intent)
    }
}