package com.example.friendzone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.friendzone.UserContent.Companion.USER_DISPLAY_NAME
import com.example.friendzone.UserContent.Companion.USER_KEY
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_upload.*


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
        val displayName = etDisplayName.text.toString().trim()
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
                        Log.i("TEST", "createUserWithEmail:success")
                        Toast.makeText(this, "Start sharing with friends!", Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser

                        val profileUpdates =
                            UserProfileChangeRequest.Builder()
                                .setDisplayName(displayName)
                                .build()

                        user?.updateProfile(profileUpdates)
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.i(
                                        "TEST",
                                        "User display name updated."
                                    )
                                }
                            }

                        // Add user UID and email to user database
                        val userSavepoint = (application as FriendZoneApp).lookupDatabaseRef.child("user").child(auth.currentUser?.uid.toString())
                        userSavepoint.child("email").setValue(auth.currentUser?.email.toString())
                        userSavepoint.child("userName").setValue(auth.currentUser?.displayName.toString())
                        userSavepoint.child("uid").setValue(auth.currentUser?.uid.toString())

                        val intent = Intent(this, UserContent::class.java).apply {
                            putExtra(USER_KEY, user)
                            putExtra(USER_DISPLAY_NAME, displayName)
                        }
                        startActivity(intent)

                    } else {
                        var errorMsg = task.exception.toString()
                        val substringIndex = errorMsg.indexOf(':')
                        errorMsg = errorMsg.substring(substringIndex + 1)

                        Log.i("TEST", errorMsg, task.exception)
                        Toast.makeText(baseContext, errorMsg,
                            Toast.LENGTH_SHORT).show()
                    }

                }
        }
    }
}
