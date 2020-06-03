package com.example.friendzone.manager

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.friendzone.model.Group
import com.example.friendzone.model.GroupList
import com.google.gson.Gson

class GroupManager(context: Context) {

    val groupListURL = "https://raw.githubusercontent.com/KJTx1/FriendZone/thomas/sampleData/listOfGroups.json"

    private val queue: RequestQueue = Volley.newRequestQueue(context)


    fun getGroups(onGroupsReady: (GroupList) -> Unit, onError: (() -> Unit)? = null) {
        val url = groupListURL
        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                // success
                val gson = Gson()
                val allGroups = gson.fromJson(response, GroupList::class.java)
                onGroupsReady(allGroups)
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