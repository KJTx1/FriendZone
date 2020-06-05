package com.example.friendzone.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class postList(
    val group: String,
    val numOfPosts: Int,
    val posts: List<Post>
) : Parcelable