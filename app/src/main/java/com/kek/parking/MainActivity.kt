package com.kek.parking

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kek.parking.constance.constances
import com.kek.parking.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var bindingClass: ActivityMainBinding
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var currentUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    fun releaseButton (view: View) {
        currentUser = auth.uid.toString()
        db.collection("parking")
            .document("$currentUser")
            .get("placeTaken")
    }


    fun parking1Clicks (view: View) {
        currentUser = auth.uid.toString()
        val callDialog = AlertDialog.Builder(this@MainActivity)
        callDialog.setTitle("${constances.RENT_TITLE}")
        callDialog.setMessage("${constances.RENT1}")
        callDialog.setIcon(R.drawable.question)
        callDialog.setPositiveButton("${constances.OK}") { dialog, id ->
            db.collection("parking")
                .document("place1")
                .update(
                    mapOf("isTaken" to true,
                "byWho" to "$currentUser"))

            db.collection("users")
                .document("$currentUser")
                .update(mapOf("placeTaken" to "place1"))

            }

        callDialog.setNegativeButton("${constances.CANCEL}") { dialog, id ->

        dialog.dismiss()
        }
        callDialog.show()

    }
}