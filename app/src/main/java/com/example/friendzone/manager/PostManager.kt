package com.example.friendzone.manager

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.friendzone.model.Post
import com.example.friendzone.model.postList
import com.google.gson.Gson

class PostManager(context: Context) {

    val postListURL = "https://raw.githubusercontent.com/KJTx1/FriendZone/thomas/sampleData/listOfPosts.json"

    private val queue: RequestQueue = Volley.newRequestQueue(context)


    fun getPosts(onPostsReady: (postList) -> Unit, onError: (() -> Unit)? = null) {
        val url = postListURL
        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                // success
                val gson = Gson()
                val allPosts = gson.fromJson(response, postList::class.java)
                onPostsReady(allPosts)
            },
            { error ->
                //error
                Log.e("info", "Error occured: ${error.networkResponse.statusCode}")
                onError?.invoke()
            }
        )
        queue.add(request)
    }

}