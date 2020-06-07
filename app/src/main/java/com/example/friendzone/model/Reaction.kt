package com.example.friendzone.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Reaction(
    val user: String? = null,
    val reaction: String? = null
) : Parcelable