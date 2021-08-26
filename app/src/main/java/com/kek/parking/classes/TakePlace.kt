package com.kek.parking.classes

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kek.parking.R
import com.kek.parking.constance.constances



class TakePlace {


    fun showDialog(context: Context,placeTaken: String, rentTaken: String) {

        var auth: FirebaseAuth = FirebaseAuth.getInstance()
        var currentUser = auth.uid.toString()
        var db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val place = placeTaken
        val rent = rentTaken
        var callDialog = AlertDialog.Builder(context)

        callDialog.setTitle("${constances.RENT_TITLE}")
        callDialog.setMessage("$rent")
        callDialog.setIcon(R.drawable.question)
        callDialog.setPositiveButton("${constances.OK}") { dialog, id ->
            db.collection("${constances.DB_PARKING}")
                .document("$place")
                .update(
                    mapOf(
                        "isTaken" to true,
                        "byWho" to "$currentUser"
                    )
                )

            db.collection("${constances.DB_USERS}")
                .document("$currentUser")
                .update(mapOf("${constances.FIELD_PLACETAKEN}" to "${constances.PLACE1}"))

            FastToast().makeToast(  context, "${constances.PLACE_IS_TAKEN_TOAST}")


        }

        callDialog.setNegativeButton("${constances.CANCEL}") { dialog, id ->

            dialog.dismiss()
        }
        callDialog.show()

    }
}