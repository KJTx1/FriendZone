package com.example.friendzone.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Reaction(
    val id: String,
    val user: String,
    val reaction: CharSequence,
    val timestamp: Date
) : Parcelable