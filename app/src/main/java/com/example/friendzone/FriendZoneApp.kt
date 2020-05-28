package com.example.friendzone

import android.app.Application
import android.content.Intent
import android.util.Log
import com.example.friendzone.manager.PostManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FriendZoneApp: Application() {
    lateinit var auth: FirebaseAuth
    lateinit var postManager: PostManager

//    lateinit var sessionManager: SessionManager

//    var user: FirebaseUser? = null

    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth
        postManager = PostManager(this)
    }
}