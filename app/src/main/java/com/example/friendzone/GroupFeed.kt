package com.example.friendzone

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.friendzone.manager.ApiManager
import com.example.friendzone.manager.PostManager
import com.example.friendzone.model.Post
import kotlinx.android.synthetic.main.activity_group_feed.*


class GroupFeed : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var postAdapter: PostAdapter
    var listOfPosts = mutableListOf<Post>()
    private lateinit var postManager: PostManager

//    private lateinit var databaseRef: DatabaseReference
    private lateinit var app: FriendZoneApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_feed)
        app = (application as FriendZoneApp)
        tvNoPost.visibility = View.GONE
        btnAdd.alpha = .5F

        val groupName = intent.getStringExtra("GROUP_NAME")
        tvGroupFeedTitle.text = groupName
        app.apiManager.fetchPost(groupName, { posts ->
            Log.i("size", posts.size.toString())
            if (posts.size == 0) {
                tvNoPost.visibility = View.VISIBLE
            }
            postAdapter = PostAdapter(posts, app)
            rvPostFeed.adapter = postAdapter
        }, {
            Toast.makeText(this, "Cannot retrieve user group members information", Toast.LENGTH_SHORT).show()
        })

        btnAdd.setOnClickListener {
            btnAdd.alpha = 1F
            startActivity(Intent(this, Upload::class.java))
        }

        btnAddPeople.setOnClickListener {
            showAddItemDialog(groupName)
        }
    }

    private fun showAddItemDialog(group: String) {
        val taskEditText = EditText(this)

        val dialog: AlertDialog = AlertDialog.Builder(this)
            .setTitle("Add friend to $group")
            .setMessage("Search FriendZone email account")
            .setView(taskEditText)
            .setPositiveButton("Add"
            ) { _, _ ->
                val user = taskEditText.text.toString()

                app.apiManager.addUser(user, group, {

//                    Toast.makeText(this, "added $user to $group", Toast.LENGTH_SHORT).show()
                }, {
//                    Toast.makeText(this, "Fail to added $user to $group", Toast.LENGTH_SHORT).show()
                },
                this,
                false)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setView(taskEditText, 50 ,0, 50 , 0)
        dialog.show()

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResume() {
        super.onResume()
        btnAdd.alpha = .5F
    }
}
