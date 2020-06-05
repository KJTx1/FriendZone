package com.example.friendzone

data class UploadManager(
     var userName: String? = null,
     var timeStamp: Long? = null,
     var shareTo: String? = null,
     var postDesc: String? = null,
     var userEmail: String? = null,
     var imageUrl: String? = null,
     var audioUrl: String? = null,
     var userID: String? = null,
     var reacation: Map<String, String>? = null
)