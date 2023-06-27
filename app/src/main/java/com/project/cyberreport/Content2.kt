package com.project.cyberreport

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*

class Content2 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content2)


        auth = FirebaseAuth.getInstance()
        val welcomeEmail: TextView = findViewById(R.id.welcomeEmail)
        val user = Firebase.auth.currentUser

        for (profile in user?.providerData!!) {
            val email: String? = profile.email
            welcomeEmail.text = email
        }

        val nextBtn: Button = findViewById(R.id.next2)
        nextBtn.setOnClickListener {
            val others: EditText = findViewById(R.id.others)
            val occured: EditText = findViewById(R.id.occured)

            val otherText = others.text.toString()
            val otherOccured = occured.text.toString()

            if (otherText.isEmpty() || otherOccured.isEmpty()){
                Toast.makeText(this, "Please input data in each field", Toast.LENGTH_SHORT).show()
            } else {
                finish()
                intent = Intent(this, Content3::class.java)
                startActivity(intent)
            }
        }

        val cybercrime = resources.getStringArray(R.array.Cybercrime)
        val spinner: Spinner = findViewById(R.id.spinner)
            if (spinner != null){
                val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, cybercrime)
                spinner.adapter = adapter
                spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        //
                    }
                    override fun onNothingSelected(parent: AdapterView<*>) {
                       //
                    }
                }
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