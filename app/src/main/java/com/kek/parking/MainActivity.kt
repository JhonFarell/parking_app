package com.kek.parking


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kek.parking.classes.FastToast
import com.kek.parking.classes.TakePlace
import com.kek.parking.constance.constances
import com.kek.parking.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var bindingClass: ActivityMainBinding
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var place: String
    lateinit var currentUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    /*Обращается в БД юзеров, оттуда вытягивает значение занято/свободно что-либо юзером, если занято -
      освобождает, в противном случае - выдает тост о том, что, сперва, нужно занять место
     Кнопка освобождения парковочного места (если оно занято)
    */

    fun releaseButton(view: View) {

        val callDialog = AlertDialog.Builder(this)
        currentUser = auth.uid.toString()

        db.collection("${constances.DB_USERS}")
            .document("$currentUser")
            .get().addOnSuccessListener {
                place = it.get("${constances.FIELD_PLACETAKEN}").toString()
                if (place.isEmpty()) {
                    FastToast().makeToast(this, "${constances.NO_PLACE_TAKEN}")
                } else {

                    callDialog.setTitle("${constances.FREE_PLACE}")
                    callDialog.setMessage("${constances.FREE_RENT}")
                    callDialog.setIcon(R.drawable.question)
                    callDialog.setPositiveButton("${constances.OK}") { dialog, id ->

                        db.collection("${constances.DB_USERS}")
                            .document("$currentUser")
                            .get().addOnSuccessListener {
                                db.collection("${constances.DB_USERS}")
                                    .document("$currentUser")
                                    .update(
                                        mapOf("${constances.FIELD_PLACETAKEN}" to "")
                                    )

                                db.collection("${constances.DB_PARKING}")
                                    .document("$place")
                                    .update(
                                        mapOf(
                                            "isTaken" to false,
                                            "byWho" to ""
                                        )
                                    )
                                FastToast().makeToast(this, "${constances.PLACE_FREED_TOAST}")

                            }
                    }


                    callDialog.setNegativeButton("${constances.CANCEL}") { dialog, id ->

                        dialog.dismiss()
                    }
                    callDialog.show()

                }
            }
    }

    /*При нажатии на иконку парковочного места, забивает за юзером выбранное место
      При попытке занять более одного места выдает тост о том, что так нельзя
      Все действия через обращения в БД
    */
    fun parkingClicks(view: View) {
            currentUser = auth.uid.toString()

            db.collection("${constances.DB_USERS}")
                .document("$currentUser")
                .get().addOnSuccessListener {
                    place = it.get("${constances.FIELD_PLACETAKEN}").toString()
                    if (place.isEmpty()) {
                        when (view.id) {
                            bindingClass.place1.id -> {TakePlace().showDialog(this,
                                "${constances.PLACE1}",
                                "${constances.RENT1}")}
                            bindingClass.place2.id -> { TakePlace().showDialog(this,
                                "${constances.PLACE2}",
                                "${constances.RENT2}")}
                            bindingClass.place3.id -> {TakePlace().showDialog(this,
                                "${constances.PLACE1}",
                                "${constances.RENT1}")}
                        }
                    }
                    else { FastToast().makeToast(this, "${constances.PLACE_TAKEN_ERROR}")}
                }
        }

}