package com.example.friendzone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*


class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = (application as FriendZoneApp).auth

        buttonRegister.setOnClickListener {
            registerUser()
        }


    }

    private fun registerUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty() && password.isEmpty()) {
            Toast.makeText(this, "Please sign up with email and password", Toast.LENGTH_SHORT).show()
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show()
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.i("Jason", "createUserWithEmail:success")
                        Toast.makeText(this, "Start sharing with friends!", Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser

                        val intent = Intent(this, UserContent::class.java).apply {
                            putExtra(UserContent.USER_KEY, user)
                        }
                        startActivity(intent)

                    } else {
                        var errorMsg = task.exception.toString()
                        val substringIndex = errorMsg.indexOf(':')
                        errorMsg = errorMsg.substring(substringIndex + 1)

                        Log.i("Jason", errorMsg, task.exception)
                        Toast.makeText(baseContext, errorMsg,
                            Toast.LENGTH_SHORT).show()
                    }

                }
        }
    }
}
