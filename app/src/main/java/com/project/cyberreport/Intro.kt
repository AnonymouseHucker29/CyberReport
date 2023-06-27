package com.project.cyberreport

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.Exception
import java.lang.NullPointerException

class Intro : AppCompatActivity() {

    private val splashScreentimeout : Long = 1500
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro)

        try {
            this.supportActionBar!!.hide()
        }
        catch (e: NullPointerException) {
        }

        auth = FirebaseAuth.getInstance()
        val user = Firebase.auth.currentUser

        Handler().postDelayed({
            if (user != null && verifConn()){
                finish()
                auth = FirebaseAuth.getInstance()
                for (profile in user.providerData) {
                    val email: String? = profile.email
                    Toast.makeText(this, "Welcome back, $email", Toast.LENGTH_SHORT).show()
                }
                intent = Intent(this, Content::class.java)
                startActivity(intent)
            }else if (!verifConn()){
                val builder = AlertDialog.Builder(this)
                builder.setMessage("No internet connection detected.")
                builder.setPositiveButton("Exit") { DialogInterface, i: Int ->
                    finish()
                }
                builder.setNegativeButton("Refresh") { DialogInterface, i: Int ->
                    finish()
                    startActivity(getIntent())
                }
                builder.show()
                builder.setCancelable(false)
            }
            else {
                finish()
                intent = Intent(this, LoginAct::class.java)
                startActivity(intent)
            }
        }, splashScreentimeout
        )

    }

    private fun verifConn(): Boolean {
        var connected: Boolean = false
        try {
            val cm =
                applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nInfo = cm.activeNetworkInfo
            connected = nInfo != null && nInfo.isAvailable && nInfo.isConnected
            return connected
        } catch (e: Exception) {
            e.message?.let { Log.e("Connectivity Exception", it) }
        }
        return connected
    }

}