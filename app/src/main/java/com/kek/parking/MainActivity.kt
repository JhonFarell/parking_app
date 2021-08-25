package com.kek.parking


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

    fun releaseButton(view: View) {
        //Кнопка освобождения парковочного места (если оно занято)
        val callDialog = AlertDialog.Builder(this)
        currentUser = auth.uid.toString()

        /*Обращается в БД юзеров, оттуда вытягивает значение занято/свободно что-либо юзером, если занято -
        освобождает, в противном случае - выдает тост о том, что, сперва, нужно занять место
         */
        db.collection(constances.DB_USERS)
            /**
             * String templates удобно использовать, когда нужно конкатенировать несколько строк
             * или вставлять их в литерал, напрмер "Мой хуй - ${myPenis.size} см"
             * В данном случае от них нет никакого профита, понятней просто обращаться к переменным и константам
             */
            .document(currentUser)
            .get().addOnSuccessListener {
                place = it.get(constances.FIELD_PLACETAKEN).toString()
                if (place.isEmpty()) {
                    Toast.makeText(this, constances.NO_PLACE_TAKEN, Toast.LENGTH_SHORT).show()
                } else {

                    callDialog.setTitle(constances.FREE_PLACE)
                    callDialog.setMessage(constances.FREE_RENT)
                    callDialog.setIcon(R.drawable.question)
                    callDialog.setPositiveButton(constances.OK) { dialog, id ->
                        db.collection(constances.DB_USERS)
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
                                Toast.makeText(
                                    this,
                                    "${constances.PLACE_FREED_TOAST}",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                            }
                    }


                    callDialog.setNegativeButton("${constances.CANCEL}") { dialog, id ->

                        dialog.dismiss()
                    }
                    callDialog.show()

                }
            }
    }

    /**
     * Описание метода (раз уж оно есть) обычно пишут перед методом, а не внутри
     * Если оформить все по красоте (подробнее тут https://kotlinlang.org/docs/kotlin-doc.html),
     * то можно генерить потом удобную документацию
     */
    fun parkingClicks(view: View) {
            /*При нажатии на иконку парковочного места, забивает за юзером выбранное место
               При попытке занять более одного места выдает тост о том, что так нельзя
               Все действия через обращения в БД
            */
            currentUser = auth.uid.toString()
            val callDialog = AlertDialog.Builder(this)

            db.collection("${constances.DB_USERS}")
                .document("$currentUser")
                .get().addOnSuccessListener {
                    place = it.get("${constances.FIELD_PLACETAKEN}").toString()
                    if (place.isEmpty()) {
                        when (view.id) {
                            /**
                             *  в этих трех кейсах явное дублирование кода, этого нужно избегать
                             *  выделяешь общую логику в отдельный метод, а то что отличается
                             *  (в данном случае константы RENT и PLACE) передаешь как параметры
                             */
                            bindingClass.place1.id -> {
                                callDialog.setTitle("${constances.RENT_TITLE}")
                                callDialog.setMessage("${constances.RENT1}")
                                callDialog.setIcon(R.drawable.question)
                                callDialog.setPositiveButton("${constances.OK}") { dialog, id ->
                                    db.collection("${constances.DB_PARKING}")
                                        .document("${constances.PLACE1}")
                                        .update(
                                            mapOf(
                                                "isTaken" to true,
                                                "byWho" to "$currentUser"
                                            )
                                        )

                                    db.collection("${constances.DB_USERS}")
                                        .document("$currentUser")
                                        .update(mapOf("${constances.FIELD_PLACETAKEN}" to "${constances.PLACE1}"))

                                    Toast.makeText(
                                        this,
                                        "${constances.PLACE_IS_TAKEN_TOAST}",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }

                                callDialog.setNegativeButton("${constances.CANCEL}") { dialog, id ->

                                    dialog.dismiss()
                                }
                                callDialog.show()

                            }
                            bindingClass.place2.id -> {
                                callDialog.setTitle("${constances.RENT_TITLE}")
                                callDialog.setMessage("${constances.RENT2}")
                                callDialog.setIcon(R.drawable.question)
                                callDialog.setPositiveButton("${constances.OK}") { dialog, id ->
                                    db.collection("${constances.DB_PARKING}")
                                        .document("${constances.PLACE2}")
                                        .update(
                                            mapOf(
                                                "isTaken" to true,
                                                "byWho" to "$currentUser"
                                            )
                                        )

                                    db.collection("${constances.DB_USERS}")
                                        .document("$currentUser")
                                        .update(mapOf("${constances.FIELD_PLACETAKEN}" to "${constances.PLACE2}"))

                                    Toast.makeText(
                                        this,
                                        "${constances.PLACE_IS_TAKEN_TOAST}",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }

                                callDialog.setNegativeButton("${constances.CANCEL}") { dialog, id ->

                                    dialog.dismiss()
                                }
                                callDialog.show()

                            }
                            bindingClass.place3.id -> {
                                callDialog.setTitle("${constances.RENT_TITLE}")
                                callDialog.setMessage("${constances.RENT3}")
                                callDialog.setIcon(R.drawable.question)
                                callDialog.setPositiveButton("${constances.OK}") { dialog, id ->
                                    db.collection("${constances.DB_PARKING}")
                                        .document("${constances.PLACE3}")
                                        .update(
                                            mapOf(
                                                "isTaken" to true,
                                                "byWho" to "$currentUser"
                                            )
                                        )

                                    db.collection("${constances.DB_USERS}")
                                        .document("$currentUser")
                                        .update(mapOf("${constances.FIELD_PLACETAKEN}" to "${constances.PLACE3}"))

                                    Toast.makeText(
                                        this,
                                        "${constances.PLACE_IS_TAKEN_TOAST}",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }

                                callDialog.setNegativeButton("${constances.CANCEL}") { dialog, id ->

                                    dialog.dismiss()
                                }
                                callDialog.show()

                            }
                        }
                    }
                    else {Toast.makeText(this, "${constances.PLACE_TAKEN_ERROR}", Toast.LENGTH_SHORT).show()}
                }
        }
}