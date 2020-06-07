package com.example.friendzone

import com.example.friendzone.model.Reaction

data class UploadManager(
     var userName: String? = null,
     var timeStamp: Long? = null,
     var shareTo: String? = null,
     var postDesc: String? = null,
     var userEmail: String? = null,
     var imageUrl: String? = null,
     var audioUrl: String? = null,
     var userID: String? = null,
     var reaction: List<Reaction> = emptyList()
)