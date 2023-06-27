package com.project.cyberreport

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class Content4 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content4)


        auth = FirebaseAuth.getInstance()
        val welcomeEmail: TextView = findViewById(R.id.welcomeEmail)
        val user = Firebase.auth.currentUser

        for (profile in user?.providerData!!) {
            val email: String? = profile.email
            welcomeEmail.text = email
        }

        val upload4: Button = findViewById(R.id.upload4)
        upload4.setOnClickListener {
            val i = Intent()
            i.setType("image/*")
            i.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(i, "Choose Picture"), 100)
        }

        val nextBtn: Button = findViewById(R.id.next4)
        nextBtn.setOnClickListener {
            uploadFile()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val imageView: ImageView = findViewById(R.id.imageupload)
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100 && data != null && data.data != null) {
            imageUri = data.data!!
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            imageView.setImageBitmap(bitmap)
        }
    }

    private fun uploadFile(){
        if (imageUri != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Uploading...")
            progressDialog.show()

            val user = Firebase.auth.currentUser?.email
            val imageRef = FirebaseStorage.getInstance().reference.child("user:$user/" + "selfiewithID")

            imageRef.putFile(imageUri)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Upload Success!", Toast.LENGTH_SHORT).show()
                    finish()
                    intent = Intent(this, Last::class.java)
                    startActivity(intent)
                }.addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Upload Failed!", Toast.LENGTH_SHORT).show()
                }.addOnProgressListener {
                    val progress = (100.0 * it.bytesTransferred / it.totalByteCount)
                    progressDialog.setMessage("${progress.toInt()}% complete...")
                }
        }
    }

}
