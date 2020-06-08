package com.example.friendzone

import android.app.Application
import com.example.friendzone.manager.ApiManager
import com.example.friendzone.manager.GroupManager
import com.example.friendzone.manager.PostManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class FriendZoneApp: Application() {
    lateinit var auth: FirebaseAuth
    lateinit var postManager: PostManager
    lateinit var groupManager: GroupManager
    lateinit var lookupDatabaseRef: DatabaseReference
    lateinit var userGroups: List<String>
//    lateinit var sessionManager: SessionManager

//    lateinit var uploadManager: UploadManager

    lateinit var internetManager: InternetManager

    lateinit var apiManager: ApiManager

    override fun onCreate() {

        super.onCreate()
        auth = Firebase.auth
        lookupDatabaseRef = FirebaseDatabase.getInstance().reference
        userGroups = emptyList()

        internetManager = InternetManager()
        postManager = PostManager(this)
        groupManager = GroupManager(this)
        apiManager = ApiManager(lookupDatabaseRef)
    }
}