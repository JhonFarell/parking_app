package com.kek.parking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.kek.parking.databinding.ActivityLoginScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.kek.parking.constance.constances


class LoginScreen : AppCompatActivity() {

    lateinit var bindingClass: ActivityLoginScreenBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        auth = FirebaseAuth.getInstance()
    }

    fun registerClick(view: View) {
        val intent = Intent(this, Registration::class.java)
        startActivity(intent)
    }

    fun logInClick(view: View) {
        var emailIsEmpty = TextUtils.isEmpty(bindingClass.loginField.text.toString().trim())
        var passwordIsEmpty = TextUtils.isEmpty(bindingClass.passwordField.text.toString().trim())

        when {
            emailIsEmpty -> {
                Toast.makeText(this, "${constances.LOGIN_FAILED}", Toast.LENGTH_SHORT)
            }
            passwordIsEmpty -> {
                Toast.makeText(this, "${constances.LOGIN_FAILED}", Toast.LENGTH_SHORT)
            }
            else -> {
                var email: String = bindingClass.loginField.text.toString().trim()
                var password: String = bindingClass.passwordField.text.toString().trim()

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            var intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "${constances.LOGIN_FAILED}", Toast.LENGTH_SHORT)
                        }

                    }
            }

        }
    }
}