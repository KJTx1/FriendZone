package com.example.friendzone.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Group(
    val name: String,
    val members: List<String>
) : Parcelable