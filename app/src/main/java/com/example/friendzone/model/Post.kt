package com.example.friendzone.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Post(
    val user: String,
    val image: String,
    val audio: String,
    val timestamp: String,
    val reactions: List<Reaction>
) : Parcelable

//@Parcelize
//data class Post (
//    var userName: String,
//    var timeStamp: Long,
//    var shareTo: String,
//    var postDesc: String,
//    var userEmail: String,
//    var imageUrl: String,
//    var audioUrl: String,
//    var userID: String,
//    var reaction: Map<String, String>
//) : Parcelable