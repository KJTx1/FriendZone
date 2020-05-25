package com.example.friendzone

import android.app.Application
import android.content.Intent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FriendZoneApp: Application() {
    lateinit var auth: FirebaseAuth

//    var user: FirebaseUser? = null

    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth

        if (auth.currentUser != null) {
            val intent = Intent(this, UserContent::class.java).apply {
                putExtra(UserContent.USER_KEY, auth.currentUser)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)

        }
    }
}