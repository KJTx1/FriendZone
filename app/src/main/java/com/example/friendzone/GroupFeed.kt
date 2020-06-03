package com.example.friendzone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import com.example.friendzone.manager.PostManager
import com.example.friendzone.model.Post
import kotlinx.android.synthetic.main.activity_group_feed.*

class GroupFeed : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var postAdapter: PostAdapter
    var listOfPosts = mutableListOf<Post>()
    private lateinit var postManager: PostManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_feed)
        postManager = (application as FriendZoneApp).postManager
        postManager.getPosts({ postLibrary ->
            Log.i("info", postLibrary.toString())
            listOfPosts = postLibrary.posts.toMutableList()
            postAdapter = PostAdapter(listOfPosts)
            rvPostFeed.adapter = postAdapter
        }, {
            Log.i("info", "Error fetching posts")
        })
    }

    fun fetchPosts() {
        postManager.getPosts({
            listOfPosts = it.posts.toMutableList()
            postAdapter.change(listOfPosts)
            rvPostFeed.adapter = postAdapter
        }, {
            Log.i("info", "Error fetching posts")
        })
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
