package com.example.friendzone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.friendzone.UserContent.Companion.USER_KEY
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = (application as FriendZoneApp).auth

        buttonRegister.setOnClickListener {
            loginUser()
        }

        tvLogin.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty() && password.isEmpty()) {
            Toast.makeText(this, "Please log up with email and password", Toast.LENGTH_SHORT).show()
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show()
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.i("Jason", "createUserWithEmail:success")
                        Toast.makeText(this, "Start sharing with friends!", Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser

                        val intent = Intent(this, UserContent::class.java).apply {
                            putExtra(USER_KEY, user)
                        }
                        startActivity(intent)

//                        startActivityForResult(intent, RC_SIGN_IN)

                    } else {
                        var errorMsg = task.exception.toString()
                        val substringIndex = errorMsg.indexOf(':')
                        errorMsg = errorMsg.substring(substringIndex + 1)

                        // If sign in fails, display a message to the user.
                        Log.i("Jason", errorMsg, task.exception)

                        Toast.makeText(baseContext, errorMsg,
                            Toast.LENGTH_SHORT).show()
                    }

                }
        }
    }
}
