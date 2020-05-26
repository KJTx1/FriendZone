package com.example.friendzone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.friendzone.model.Post

class GroupFeed : AppCompatActivity() {

    private lateinit var postAdapter: PostAdapter
    private lateinit var listOfPosts: MutableList<Post>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_feed)
    }
}
