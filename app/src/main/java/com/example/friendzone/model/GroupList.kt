package com.example.friendzone.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GroupList(
    val groupList: List<Group>
) : Parcelable