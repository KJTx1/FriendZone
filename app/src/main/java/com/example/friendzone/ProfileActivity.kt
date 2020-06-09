package com.example.friendzone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }

    fun changeClicked(view: View) {

//        val tvUsername = findViewById<TextView>(R.id.tvUsername)
//        val btnChange = findViewById<Button>(R.id.btnChange)
//        val etUsername = findViewById<EditText>(R.id.etUsername)
//
//        if (btnChange.text == "Change User") {
//            tvUsername.setVisibility(View.INVISIBLE)
//            etUsername.setVisibility(View.VISIBLE)
//            val inputedName = etUsername.text.toString()
//            btnChange.text = "Apply"
//        } else {
//            btnChange.text = "Change User"
//            tvUsername.setText(etUsername.text.toString())
//            tvUsername.setVisibility(View.VISIBLE)
//            etUsername.setVisibility(View.INVISIBLE)
//        }
    }
}
