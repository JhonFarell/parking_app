package com.kek.parking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.kek.parking.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private  var firstTimeUser = true
    lateinit var bindingClass: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        auth = FirebaseAuth.getInstance()

    }

    fun buttonClicks (view: View) {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)

    }
}