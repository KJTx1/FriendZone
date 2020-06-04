package com.example.friendzone

data class Groupspace(
    val id: String,
    val displayName: String,
    val members: List<User>
)