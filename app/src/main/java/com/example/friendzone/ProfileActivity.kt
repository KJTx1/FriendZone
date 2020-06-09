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

        val btnEdit = findViewById<Button>(R.id.btnEdit)

        btnEdit.setOnClickListener {
            changeClicked(btnEdit)
        }
    }

    private fun changeClicked(btnEdit: Button) {
        val etName = findViewById<TextView>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)

    }
}
