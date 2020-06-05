package com.example.friendzone.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Post(
    val id: String,
    val user: String,
    val image: String,
    val audio: String,
    val timestamp: String,
    val reactions: List<Reaction>
) : Parcelable