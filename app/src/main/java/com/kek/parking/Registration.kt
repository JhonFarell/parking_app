package com.kek.parking


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.kek.parking.constance.constances
import com.kek.parking.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class Registration : AppCompatActivity() {
    lateinit var bindingClass: ActivityRegistrationBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        auth = FirebaseAuth.getInstance()

    }

    fun closeActivity (view: View) {
            finish()
    }

    fun regButton (view: View) {
            var emailIsEmpty = TextUtils.isEmpty(bindingClass.emailReg.text.toString().trim())
            var passwordIsEmpty = TextUtils.isEmpty(bindingClass.passwordReg.text.toString().trim())

            when {
                emailIsEmpty -> {
                    Toast.makeText(
                        this@Registration,
                        "${constances.EMAIL_ERROR_MESSAGE}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                passwordIsEmpty -> {
                    Toast.makeText(
                        this@Registration,
                        "${constances.PWORD_ERROR_MESSAGE}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    val email: String = bindingClass.emailReg.text.toString().trim()
                    val pword: String = bindingClass.passwordReg.text.toString().trim()

                    auth.createUserWithEmailAndPassword(email, pword)
                        .addOnCompleteListener{ task ->
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser = task.result!!.user!!

                            Toast.makeText(
                                this@Registration,
                                "${constances.REGISTER_SUCCESS}",
                                Toast.LENGTH_SHORT
                            ).show()
                                finish()

                        }
                            else {
                                Toast.makeText(
                                    this@Registration,
                                    "${constances.REGISTER_FAILED}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }

            }

    }

}