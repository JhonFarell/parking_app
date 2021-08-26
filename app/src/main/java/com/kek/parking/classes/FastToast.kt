package com.kek.parking.classes

import android.content.Context
import android.view.Gravity
import android.widget.Toast

class FastToast {
    fun makeToast (context: Context,text: String) {
        var context = context
        var text = text
        var toast = Toast.makeText(context, "$text", Toast.LENGTH_SHORT)

        toast.setGravity(Gravity.CENTER, 0, 250)
        toast.show()


    }
}